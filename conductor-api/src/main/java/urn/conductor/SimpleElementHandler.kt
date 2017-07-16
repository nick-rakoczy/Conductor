package urn.conductor

import javax.xml.bind.JAXBElement
import javax.xml.bind.annotation.XmlElementDecl
import javax.xml.namespace.QName
import kotlin.reflect.KFunction

interface SimpleElementHandler {

	val handles: QName

	fun process(element: Any, engine: Engine)

	companion object {
		fun <T> fromFactory(factory: KFunction<JAXBElement<T>>): QName {
			return factory.annotations
					.filterIsInstance(XmlElementDecl::class.java)
					.singleOrNull()
					?.let { QName(it.namespace, it.name) }
					?: error("Not a JAXBElement factory method.")
		}
	}
}