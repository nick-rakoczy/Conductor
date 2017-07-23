package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.Engine
import urn.conductor.SimpleElementHandler
import urn.conductor.stdlib.xml.ObjectFactory
import javax.xml.namespace.QName

class LogHandler : SimpleElementHandler {
	private val logger = LogManager.getLogger("Log")

	override val handles: QName
		get() = SimpleElementHandler.fromFactory(ObjectFactory::createLog)

	override fun process(element: Any, engine: Engine) {
		logger.info(element as String)
	}
}