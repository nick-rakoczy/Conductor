package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Load
import java.nio.file.Files

class LoadHandler : ElementHandler<Load> {
	private val logger = LogManager.getLogger()

	override val handles: Class<Load>
		get() = Load::class.java

	override fun process(element: Load, engine: Engine, processChild: (Any) -> Unit) {
		val charset = element.charset
				?.let { charset(it) }
				?: Charsets.UTF_8

		val sourcePath = engine.getPath(element.src.let(engine::interpolate))
		val data = Files.readAllBytes(sourcePath).toString(charset)
		val type = element.mode

		when (type) {
			"text" -> engine.put(element.id, data)
			"json" -> engine.put(element.id, engine.gson.fromJson(data, Any::class.java))
			"js" -> engine.eval("var ${element.id} = $data;")
		}

		logger.info("Loaded ${type.toUpperCase()} [${sourcePath.toAbsolutePath().normalize()}] as ${element.id}")
	}
}
