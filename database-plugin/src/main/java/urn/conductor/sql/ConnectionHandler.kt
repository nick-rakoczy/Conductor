package urn.conductor.sql

import org.apache.logging.log4j.LogManager
import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.database.xml.Connection
import java.sql.DriverManager

class ConnectionHandler : ComplexElementHandler<Connection> {
	override val handles: Class<Connection>
		get() = Connection::class.java

	private val logger = LogManager.getLogger()

	override fun process(element: Connection, engine: Engine, processChild: (Any) -> Unit) {
		element.apply {
			val connectionString = element.connection.let(engine::interpolate)
			val connection = DriverManager.getConnection(connectionString, element.username, element.password)
			engine.put(element.id, connection)

			logger.info("Connected to JDBC Database <$connectionString> as [${element.id}]")
		}
	}
}