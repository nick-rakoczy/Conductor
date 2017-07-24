package urn.conductor.core

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Container

class ContainerHandler : ComplexElementHandler<Container> {
	override fun process(element: Container, engine: Engine, processChild: (Any) -> Unit) {
		element.any.forEach(processChild)
	}

	override val handles: Class<Container>
		get() = Container::class.java
}