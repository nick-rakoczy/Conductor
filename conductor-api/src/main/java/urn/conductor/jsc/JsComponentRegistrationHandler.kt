package urn.conductor.jsc

import jdk.internal.dynalink.beans.StaticClass
import urn.conductor.Engine

interface JsComponentRegistrationHandler {
	fun registerAttributeHandler(namespace: String, localName: String, priority: Int, handler: (Any, String, Engine, () -> Unit) -> Unit)
	fun registerSimpleElementHandler(namespace: String, localName: String, handler: (Any, Engine) -> Unit)
	fun <T : Any> registerComplexElementHandler(type: StaticClass, handler: (T, Engine, (Any) -> Unit) -> Unit)
	fun registerPreloader(priority: Int, preloadFunction: (Engine) -> Unit)
}