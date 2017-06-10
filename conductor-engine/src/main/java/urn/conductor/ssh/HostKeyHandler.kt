package urn.conductor.ssh

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.stdlib.xml.HostKey

class HostKeyHandler : ElementHandler<HostKey> {
	override val handles: Class<HostKey>
		get() = HostKey::class.java

	private val logger = LogManager.getLogger()

	override fun process(element: HostKey, engine: Engine, processChild: (Any) -> Unit) {
		val host = element.hostRef.let(engine::get) as? Host ?: error("host-ref does not point to a valid host record.")
		val value = element.value.let(engine::interpolate)

		val fingerprint = engine.sessionProvider.registerHostKey(host.address, HostKeyType.valueOf(element.type), value)
		logger.info("Registering host key for $host [$fingerprint]")
	}
}