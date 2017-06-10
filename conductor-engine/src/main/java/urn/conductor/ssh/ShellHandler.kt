package urn.conductor.ssh

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.SessionManager
import urn.conductor.absolutePathString
import urn.conductor.stdlib.xml.Shell
import java.nio.file.Files

class ShellHandler : HostIdentityElementHandler<Shell>(Shell::getHostRef, Shell::getIdentityRef) {
	override val handles: Class<Shell>
		get() = Shell::class.java

	override fun process(element: Shell, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		val script = element.value.trimStart().trimEnd().trimIndent()
		val scriptFile = Files.createTempFile("conductor-", "-script-tmp.sh")
		val scriptName = scriptFile.fileName.toString()
		Files.write(scriptFile, script.toByteArray(Charsets.UTF_8))

		val tmpFileName = "/tmp/$scriptName"

		SessionManager.getTransport(host, identity).use {
			this.upload(scriptFile, tmpFileName)
			this.execute("/bin/sh $tmpFileName")
			this.remove(tmpFileName)
		}

		scriptFile.toFile().deleteOnExit()
	}
}