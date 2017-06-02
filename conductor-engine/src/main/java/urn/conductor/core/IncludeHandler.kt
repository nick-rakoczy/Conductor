package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Include
import java.nio.file.Files

class IncludeHandler : ElementHandler<Include> {
	override val handles: Class<urn.conductor.stdlib.xml.Include>
		get() = urn.conductor.stdlib.xml.Include::class.java
	
	override fun process(element: urn.conductor.stdlib.xml.Include, engine: Engine, processChild: (Any) -> Unit) {
		val sourceFile = engine.getPath(element.src.let(engine::interpolate))
		val sourceDirectory = sourceFile.parent

		Files.newInputStream(sourceFile).use {
			engine.jaxbReader.unmarshal(it).let {
				engine.pushWorkingDirectory(sourceDirectory)
				processChild(it)
				engine.popWorkingDirectory()
			}
		}
	}
}
