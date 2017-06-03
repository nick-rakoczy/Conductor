package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.For

class ForHandler : ElementHandler<For> {
	override val handles: Class<urn.conductor.stdlib.xml.For>
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