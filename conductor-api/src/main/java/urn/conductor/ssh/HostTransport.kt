package urn.conductor.ssh

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.Session

interface HostTransport {
	val session: Session
	val properties: MutableMap<String, Any>

	fun <T> useExecChannel(block: ChannelExec.() -> T): T
	fun <T> useSftpChannel(block: ChannelSftp.() -> T): T
}