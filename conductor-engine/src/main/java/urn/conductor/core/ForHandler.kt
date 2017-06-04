package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.DebugContext
import urn.conductor.stdlib.xml.For
import javax.xml.namespace.QName

class ForHandler : ElementHandler<For> {
	private val logger = LogManager.getLogger()

	override val handles: Class<For>
		get() = urn.conductor.stdlib.xml.For::class.java

	override fun process(element: urn.conductor.stdlib.xml.For, engine: Engine, processChild: (Any) -> Unit) {
		val itemName = element.`as`
		val collectionName = element.ref
		val sourceCollection = engine.getObjectMirror(collectionName)

		val oldValue = engine.getObjectMirror(itemName)
		sourceCollection?.forEach {
			engine.put(itemName, it.value)
			element.any.forEach(processChild)
		}

		engine.delete(itemName)
		engine.put(itemName, oldValue)
	}

}