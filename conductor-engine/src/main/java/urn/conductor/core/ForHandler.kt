package urn.conductor.core

import jdk.nashorn.api.scripting.ScriptObjectMirror
import org.apache.logging.log4j.LogManager
import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.For

class ForHandler : ComplexElementHandler<For> {
	private val logger = LogManager.getLogger()

	override val handles: Class<For>
		get() = For::class.java

	override fun process(element: For, engine: Engine, processChild: (Any) -> Unit) {
		val itemName = element.id
		val collectionName = element.iteratorRef
		val sourceCollection = engine.get(collectionName)

		val oldValue = engine.get(itemName)

		fun processElement(value: Any?) {
			engine.put(itemName, value)
			element.any.forEach(processChild)
		}

		when (sourceCollection) {
			is ScriptObjectMirror -> sourceCollection
					.map { it.value }
					.forEach(::processElement)
			is Collection<*> -> sourceCollection
					.forEach(::processElement)
		}

		engine.delete(itemName)
		engine.put(itemName, oldValue)
	}

}