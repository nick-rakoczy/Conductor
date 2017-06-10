package urn.conductor.sql

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.database.xml.Query
import urn.conductor.database.xml.Update
import java.sql.Connection

class UpdateHandler : ElementHandler<Update> {
	override val handles: Class<Update>
		get() = Update::class.java

	override fun process(element: Update, engine: Engine, processChild: (Any) -> Unit) {
		val connection = engine.get(element.connectionRef) as Connection
		val result = connection.prepareStatement(element.value).executeUpdate()
	}

}