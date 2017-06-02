package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Load
import java.nio.file.Files

class LoadHandler : ElementHandler<Load> {
	override val handles: Class<urn.conductor.stdlib.xml.Load>
		get() = urn.conductor.stdlib.xml.Load::class.java

	override fun process(element: urn.conductor.stdlib.xml.Load, engine: Engine, processChild: (Any) -> Unit) {
		val charset = element.charset
				?.let { charset(it) }
				?: Charsets.UTF_8

		val sourcePath = engine.getPath(element.src.let(engine::interpolate))
		val data = Files.readAllBytes(sourcePath).toString(charset)

		engine.put(element.`as`, data)

		println("Loaded [${sourcePath.toAbsolutePath().normalize()}] as ${element.`as`}")
	}
}
