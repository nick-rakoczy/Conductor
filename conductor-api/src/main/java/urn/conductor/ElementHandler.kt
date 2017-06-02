package urn.conductor

import javax.xml.namespace.QName

interface ElementHandler<T : Any> {

	val handles: Class<T>

	fun getAttributes(element: T): Map<QName, String> = emptyMap()

	fun process(element: T, engine: Engine, processChild: (Any) -> Unit)

}