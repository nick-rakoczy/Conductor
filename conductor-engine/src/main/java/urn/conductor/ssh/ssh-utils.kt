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