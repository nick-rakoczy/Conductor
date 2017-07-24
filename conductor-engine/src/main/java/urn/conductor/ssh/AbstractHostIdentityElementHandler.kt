package urn.conductor.ssh

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity

abstract class AbstractHostIdentityElementHandler<T : Any>(
		private val hostRef: T.() -> String?,
		private val identityRef: T.() -> String?
) : ComplexElementHandler<T> {
	final override fun process(element: T, engine: Engine, processChild: (Any) -> Unit) {
		val host = element.hostRef()
				?.let(engine::get)
				as? Host
				?: error("Invalid host defined: ${element.hostRef()}")
		val identity = element.identityRef()
				?.let(engine::get)
				as? Identity
				?: error("Invalid identity defined: ${element.identityRef()}")

		process(element, engine, processChild, host, identity)
	}

	abstract fun process(element: T, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity)
}