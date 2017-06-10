package urn.conductor.ssh

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.Session
import org.apache.logging.log4j.LogManager
import urn.conductor.absolutePathString
import urn.conductor.attempt
import java.io.ByteArrayOutputStream
import java.lang.reflect.InvocationTargetException
import java.nio.file.Path

private fun waitTill(interval: Long, block: () -> Boolean) {
	while (!block()) {
		Thread.sleep(interval)
	}
}

private fun Session.openShellChannel() = this.openChannel("shell") as ChannelShell
private fun Session.openExecChannel() = this.openChannel("exec") as ChannelExec
private fun Session.openSftpChannel() = this.openChannel("sftp") as ChannelSftp

private fun <T : Channel, X> (T.() -> X).processBlock(recv: T): X {
	try {
		recv.connect()
		return this.invoke(recv)
	}
	finally {
		recv.disconnect()
	}
}

private fun <T> Session.useShell(block: ChannelShell.() -> T): T {
	return block.processBlock(this.openShellChannel())
}

private fun <T> Session.useSftp(block: ChannelSftp.() -> T): T {
	return block.processBlock(this.openSftpChannel())
}

data class PasswdRecord(val username: String,
						val password: String,
						val uid: Int,
						val primaryGid: Int,
						val comment: String,
						val homeDirectory: String,
						val shell: String) {
	companion object {
		fun parse(line: String): PasswdRecord {
			val parts = line.split(":")
			val (us, pw, uid, gid, comm) = parts
			val home = parts[5]
			val shell = parts[6]
			return PasswdRecord(us, pw, uid.toInt(), gid.toInt(), comm, home, shell)
		}
	}
}

data class GroupRecord(val name: String,
					   val password: String,
					   val gid: Int,
					   val users: List<String>) {
	companion object {
		fun parse(line: String): GroupRecord {
			val (name, pw, gid, users) = line.split(":")
			return GroupRecord(name, pw, gid.toInt(), users.split(","))
		}
	}
}

class SimpleSession(private val session: Session) {
	private val logger = LogManager.getLogger()

	fun upload(source: Path, target: String) {
		session.useSftp {
			this.put(source.absolutePathString, target)
		}
		logger.debug("put: ${source.absolutePathString} -> $target")
	}

	fun download(source: String, target: Path) {
		session.useSftp {
			this.get(source, target.absolutePathString)
		}
		logger.debug("get: $source -> ${target.absolutePathString}")
	}

	fun chmod(file: String, permissions: Int) {
		session.useSftp {
			this.chmod(permissions, file)
		}
		logger.debug("chmod: [${permissions.toString(8)}] $file")
	}

	fun symlink(path: String, target: String) {
		session.useSftp {
			this.symlink(target, path)
		}
		logger.debug("symlink: $target -> $path")
	}

	fun chgrp(path: String, group: String) {
		session.useSftp {
			val gid = getGroupId(group) ?: error("Unknown group: $group")
			this.chgrp(gid, path)
		}
		logger.debug("chgrp: [$group] $path")
	}

	fun chown(path: String, owner: String) {
		session.useSftp {
			val uid = getUserId(owner) ?: error("Unknown user: $owner")
			this.chown(uid, path)
		}
		logger.debug("chown: [$owner] $path")
	}

	fun getUserId(name: String): Int? {
		val results = this.execute("getent passwd $name")
		return attempt { PasswdRecord.parse(results).uid }
	}

	fun getGroupId(name: String): Int? {
		val results = this.execute("getent group $name")
		return attempt { GroupRecord.parse(results).gid }
	}

	fun mkdir(name: String) {
		session.useSftp {
			this.mkdir(name)
		}
	}

	fun md5(name: String): String {
		return execute("openssl dgst -md5 -hex $name").substringAfter("=").trim()
	}

	fun execute(file: String): String {
		return session.openExecChannel().run {
			this.setCommand(file)
			this.setInputStream(null)

			val errStream = ByteArrayOutputStream()
			val outStream = ByteArrayOutputStream()
			this.setErrStream(errStream)
			this.setOutputStream(outStream)

			this.connect()

			waitTill(100) {
				this.isClosed
			}

			this.disconnect()

			if (errStream.size() > 0) {
				throw InvocationTargetException(null, errStream.toString())
			}

			logger.debug("exec: $file")

			outStream.toString()
		}
	}

	fun remove(file: String) {
		session.useSftp {
			this.rm(file)
		}
		logger.debug("rm: $file")
	}
}

fun Session.use(block: SimpleSession.() -> Unit) {
	SimpleSession(this).block()
}