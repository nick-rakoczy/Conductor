package urn.conductor

import javax.xml.namespace.QName

interface AttributeHandler {

	val handles: QName

	val priority: Int

	fun process(element: Any, attribute: String, engine: Engine, proceed: () -> Unit)

}