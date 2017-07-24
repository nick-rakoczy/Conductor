package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Include
import java.nio.file.Files

class IncludeHandler : ComplexElementHandler<Include> {
	private val logger = LogManager.getLogger()

	override val handles: Class<Include>
		get() = Include::class.java

	override fun process(element: Include, engine: Engine, processChild: (Any) -> Unit) {
		val sourceFile = engine.getPath(element.src.let(engine::interpolate))
		val sourceDirectory = sourceFile.parent
		val type = element.mode

		Files.newInputStream(sourceFile).use {
			when (type) {
				"xml" -> engine.jaxbReader.unmarshal(it).let {
					engine.pushWorkingDirectory(sourceDirectory)
					processChild(it)
					engine.popWorkingDirectory()
				}
				"js" -> it.reader().let(engine::eval)
				else -> error("invalid include type")
			}
		}

		logger.info("Included ${type.toUpperCase()} [${sourceFile.toAbsolutePath().normalize()}]")
	}
}
