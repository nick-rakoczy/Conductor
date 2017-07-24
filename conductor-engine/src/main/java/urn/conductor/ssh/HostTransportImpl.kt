package urn.conductor.ssh

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.Session
import org.apache.logging.log4j.LogManager
import urn.conductor.attempt
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeoutException

class HostTransportImpl(override val session: Session) : HostTransport {
	override val properties: MutableMap<String, Any> = HashMap<String, Any>()

	private inline fun <T> tryFinally(finally: () -> Unit, block: () -> T): T {
		try {
			return block()
		}
		finally {
			finally()
		}
	}

	override fun <T> useExecChannel(block: ChannelExec.() -> T): T {
		val channelExec = this.session.openChannel("exec") as ChannelExec
		return channelExec.block()
	}

	override fun <T> useSftpChannel(block: ChannelSftp.() -> T): T {
		val channelSftp = this.session.openChannel("sftp") as ChannelSftp
		return tryFinally({ channelSftp.disconnect() }) {
			channelSftp.connect()
			channelSftp.block()
		}
	}

	override fun execute(path: String,
						 errorHandler: ((String) -> Unit),
						 pollingInterval: Long,
						 timeout: Long): String {
		val outputStream = ByteArrayOutputStream()
		val errorStream = ByteArrayOutputStream()
		val logger = LogManager.getLogger(HostTransport::class.java)

		this.useExecChannel {
			this.setOutputStream(outputStream)
			this.setErrStream(errorStream)
			this.setInputStream(null)

			this.setCommand(path)

			val startTime = System.currentTimeMillis()
			val hupTime = startTime + timeout
			val killTime = hupTime + timeout
			val abortTime = killTime + timeout

			this.connect()

			var status = "OK"

			while (!this.isClosed && status != "ABORTED") {
				Thread.sleep(pollingInterval)

				when {
					System.currentTimeMillis() > hupTime -> {
						this.sendSignal("15")
						status = "HUP"
					}
					System.currentTimeMillis() > killTime -> {
						this.sendSignal("9")
						status = "KILL"
					}
					System.currentTimeMillis() > abortTime -> {
						status = "ABORTED"
					}
				}
			}

			this.disconnect()

			val endTime = System.currentTimeMillis()

			if (errorStream.size() > 0) {
				errorHandler(errorStream.toString())
			}

			logger.info("Executed [$path] in ${endTime - startTime}msec, result: $status")

			if (status != "OK") {
				throw TimeoutException("Timed Out")
			}
		}

		return outputStream.toString()
	}

	override fun getUserId(name: String): Int? {
		return attempt {
			val (_, _, uid) = this.execute("getent passwd $name").split(":")
			uid.toIntOrNull()
		}
	}

	override fun getGroupId(name: String): Int? {
		return attempt {
			val (_, _, gid) = this.execute("getent group $name").split(":")
			gid.toIntOrNull()
		}
	}
}
