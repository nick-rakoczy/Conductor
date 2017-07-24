package urn.conductor.ssh

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine

interface TransportComplexElementHandler<T : Any> : ComplexElementHandler<T> {
	fun process(element: T, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport)
	fun getHostRef(element: T): String
	fun getIdentityRef(element: T): String
}