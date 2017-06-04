package urn.conductor

import org.apache.logging.log4j.LogManager
import javax.xml.namespace.QName

interface ElementHandler<T : Any> {

	val handles: Class<T>

	fun getAttributes(element: T): Map<QName, String>

	fun process(element: T, engine: Engine, processChild: (Any) -> Unit)

}