package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Host


class HostHandler : ElementHandler<Host> {
	override val handles: Class<urn.conductor.stdlib.xml.Host>
		get() = urn.conductor.stdlib.xml.Host::class.java

	override fun process(element: urn.conductor.stdlib.xml.Host, engine: Engine, processChild: (Any) -> Unit) {
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