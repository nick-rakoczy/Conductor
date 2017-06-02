package urn.conductor

interface HostTransport {

	fun pullFile(remotePath: String, localPath: String)
	fun pushFile(localPath: String, remotePath: String)

	fun execute(command: String)

}