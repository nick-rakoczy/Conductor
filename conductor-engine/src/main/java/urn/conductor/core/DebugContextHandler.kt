package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.stdlib.xml.DebugContext
import javax.xml.namespace.QName

class DebugContextHandler : ElementHandler<DebugContext> {
	override fun getAttributes(element: DebugContext): Map<QName, String> {
		return element.otherAttributes
	}

	private val logger = LogManager.getLogger()

	override fun process(element: urn.conductor.stdlib.xml.DebugContext, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		logger.debug(engine.toString())
	}

	override val handles: Class<urn.conductor.stdlib.xml.DebugContext>
		get() = urn.conductor.stdlib.xml.DebugContext::class.java
}