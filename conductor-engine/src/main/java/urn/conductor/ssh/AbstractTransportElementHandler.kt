package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import java.nio.file.Path


abstract class AbstractTransportElementHandler<T : Any>(
		hostRef: T.() -> String?,
		identityRef: T.() -> String?
) : AbstractHostIdentityElementHandler<T>(hostRef, identityRef) {
	final override fun process(element: T, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		engine.sessionProvider.getTransport(host, identity).let {
			process(element, engine, processChild, it)
		}
	}

	abstract fun process(element: T, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport)

	companion object {
		val Path.absolutePathString get() = this.toAbsolutePath().normalize().toString()
	}
}