package urn.conductor.sql

import org.apache.logging.log4j.LogManager
import urn.conductor.Engine
import urn.conductor.StandardComplexElementHandler
import urn.conductor.database.xml.Close
import java.sql.Connection

class CloseHandler : StandardComplexElementHandler<Close> {
	override val handles: Class<Close>
		get() = Close::class.java

	private val logger = LogManager.getLogger()

	override fun process(element: Close, engine: Engine, processChild: (Any) -> Unit) {
		val connection = engine.get(element.connectionRef) as Connection

		if (!connection.isClosed) {
			connection.close()
		}

		logger.info("Closed connection [${element.connectionRef}]")
	}
}