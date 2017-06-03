package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Host
import javax.xml.namespace.QName


class HostHandler : ElementHandler<Host> {
	private val logger = LogManager.getLogger()

	override val handles: Class<Host>
		get() = Host::class.java

	override fun getAttributes(element: Host): Map<QName, String> {
		return element.otherAttributes
	}

	override fun process(element: Host, engine: Engine, processChild: (Any) -> Unit) {
		urn.conductor.Host(
				name = element.`as`,
				ipAddress = element.ipAddress.let(engine::interpolate),
				sshPort = element.sshPort,
				tags = element.tags.map(engine::interpolate).toSet()
		).let {
			engine.put(element.`as`, it)
		}
	}
}