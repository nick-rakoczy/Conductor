package urn.conductor.sql

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.database.xml.Load
import java.nio.file.Files
import java.sql.Connection

class LoadHandler : ComplexElementHandler<Load> {
	override val handles: Class<Load>
		get() = Load::class.java

	override fun process(element: Load, engine: Engine, processChild: (Any) -> Unit) {
		val queryPath = element.src.let(engine::interpolate).let(engine::getPath)
		val queryContent = Files.readAllBytes(queryPath).toString(Charsets.UTF_8)

		val connection = engine.get(element.connectionRef) as Connection
		connection.prepareStatement(queryContent).execute()
	}
}