package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Shell
import java.nio.file.Files

class ShellHandler : TransportComplexElementHandler<Shell> {
	override fun getHostRef(element: Shell): String = element.hostRef
	override fun getIdentityRef(element: Shell): String = element.identityRef

	override val handles: Class<Shell>
		get() = Shell::class.java

	override fun process(element: Shell, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		val script = element.value.trimStart().trimEnd().trimIndent()
		val scriptFile = Files.createTempFile("conductor-", "-script-tmp.sh")
		val scriptName = scriptFile.fileName.toString()
		Files.write(scriptFile, script.toByteArray(Charsets.UTF_8))

		val tmpFileName = "/tmp/$scriptName"

		transport.useSftpChannel {
			this.put(scriptFile.toAbsolutePath().normalize().toString(), tmpFileName)
			transport.execute("/bin/sh $tmpFileName")
			this.rm(tmpFileName)
		}

		scriptFile.toFile().deleteOnExit()
	}
}