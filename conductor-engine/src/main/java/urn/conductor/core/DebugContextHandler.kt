package urn.conductor.core

import org.apache.logging.log4j.LogManager

class DebugContextHandler : urn.conductor.ElementHandler<urn.conductor.stdlib.xml.DebugContext> {
	private val logger = LogManager.getLogger()

	override fun process(element: urn.conductor.stdlib.xml.DebugContext, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		logger.debug(engine.toString())
	}

	override val handles: Class<urn.conductor.stdlib.xml.DebugContext>
		get() = urn.conductor.stdlib.xml.DebugContext::class.java
}