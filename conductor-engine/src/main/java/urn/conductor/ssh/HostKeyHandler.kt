package urn.conductor.ssh

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.SessionManager
import urn.conductor.stdlib.xml.HostKey

class HostKeyHandler : ElementHandler<HostKey> {
	override val handles: Class<HostKey>
		get() = HostKey::class.java

	private val logger = LogManager.getLogger()

	override fun process(element: HostKey, engine: Engine, processChild: (Any) -> Unit) {
		val host = element.hostRef.let(engine::get) as? Host ?: error("host-ref does not point to a valid host record.")
		val value = element.value.let(engine::interpolate)
		SessionManager.registerHostKey(host.address, element.type, value).also {
			logger.info("Registering host key for $host [$it]")
		}
	}
}