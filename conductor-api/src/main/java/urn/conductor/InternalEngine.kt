package urn.conductor

import javax.xml.bind.JAXBContext

interface InternalEngine {
	fun createJaxbContext(): JAXBContext

	fun executePreloaders(engine: Engine)

	fun runHandlerFor(element: Any, engine: Engine, proceed: (Any) -> Unit)
}