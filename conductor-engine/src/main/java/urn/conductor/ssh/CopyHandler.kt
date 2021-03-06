package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Copy
import java.nio.file.Files

class CopyHandler : TransportComplexElementHandler<Copy> {
	override fun getHostRef(element: Copy): String = element.hostRef
	override fun getIdentityRef(element: Copy): String = element.identityRef

	override val handles: Class<Copy>
		get() = Copy::class.java

	override fun process(element: Copy, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
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

		transport.useSftpChannel {
			this.put(tempFile.toAbsolutePath().normalize().toString(), element.dst)
		}

		tempFile.toFile().deleteOnExit()
	}
}