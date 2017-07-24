package urn.conductor.ssh

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.Session
import java.lang.reflect.InvocationTargetException

interface HostTransport {
	val session: Session
	val properties: MutableMap<String, Any>

	fun <T> useExecChannel(block: ChannelExec.() -> T): T
	fun <T> useSftpChannel(block: ChannelSftp.() -> T): T

	fun execute(path: String,
				errorHandler: ((String) -> Unit) = { throw InvocationTargetException(null, it) },
				pollingInterval: Long = 500,
				timeout: Long = 30_000): String

	fun getUserId(name: String): Int?

	fun getGroupId(name: String): Int?
}