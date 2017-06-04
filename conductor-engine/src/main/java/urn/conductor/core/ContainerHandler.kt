package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Container
import javax.xml.namespace.QName

class ContainerHandler : ElementHandler<Container> {
	override fun process(element: Container, engine: Engine, processChild: (Any) -> Unit) {
		element.any.forEach(processChild)
	}

	override fun getAttributes(element: Container): Map<QName, String> = element.otherAttributes

	override val handles: Class<Container>
		get() = Container::class.java
}