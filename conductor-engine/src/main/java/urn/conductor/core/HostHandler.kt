package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.internal.HostImpl
import urn.conductor.stdlib.xml.Host


class HostHandler : ComplexElementHandler<Host> {
	private val logger = LogManager.getLogger()

	override val handles: Class<Host>
		get() = Host::class.java

	override fun process(element: Host, engine: Engine, processChild: (Any) -> Unit) {
		HostImpl(
				name = element.id.let(engine::interpolate),
				address = element.address.let(engine::interpolate),
				sshPort = element.sshPort.let(engine::interpolate).toIntOrNull() ?: error("Invalid port: ${element.sshPort}"),
				tags = element.tags.map(engine::interpolate).toSet()
		).let {
			engine.put(element.id, it)
		}
	}
}