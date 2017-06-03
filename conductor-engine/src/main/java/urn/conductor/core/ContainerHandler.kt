package urn.conductor.core

class ContainerHandler : urn.conductor.ElementHandler<urn.conductor.stdlib.xml.Container> {
	override fun process(element: urn.conductor.stdlib.xml.Container, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		element.any.forEach(processChild)
	}

	override fun getAttributes(element: urn.conductor.stdlib.xml.Container): Map<javax.xml.namespace.QName, String> = element.otherAttributes

	override val handles: Class<urn.conductor.stdlib.xml.Container>
		get() = urn.conductor.stdlib.xml.Container::class.java
}