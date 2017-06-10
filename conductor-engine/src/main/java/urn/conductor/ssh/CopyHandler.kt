package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.SessionManager
import urn.conductor.stdlib.xml.Copy
import java.nio.file.Files
import java.nio.file.Paths

class CopyHandler : HostIdentityElementHandler<Copy>(Copy::getHostRef, Copy::getIdentityRef) {
	override val handles: Class<Copy>
		get() = Copy::class.java

	override fun process(element: Copy, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		val tempFile = Files.createTempFile("conductor-copy-", ".tmp")

		element.src.let(engine::getPath).let {
			Files.readAllLines(it)
		}.map {
			if (element.isTemplateParsingDisabled) {
				it
			} else {
				engine.interpolate(it)
			}
		}.let {
			Files.write(tempFile, it)
		}

		SessionManager.getTransport(host, identity).use {

			this.upload(tempFile, element.dst)
		}

		tempFile.toFile().deleteOnExit()
	}
}