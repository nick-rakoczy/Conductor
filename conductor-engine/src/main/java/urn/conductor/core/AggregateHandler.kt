package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.Engine
import urn.conductor.StandardComplexElementHandler
import urn.conductor.stdlib.xml.Aggregate

class AggregateHandler : StandardComplexElementHandler<Aggregate> {
	private val logger = LogManager.getLogger()

	override val handles: Class<Aggregate>
		get() = Aggregate::class.java

	override fun process(element: Aggregate, engine: Engine, processChild: (Any) -> Unit) {
		val results = ArrayList<Any>()

		element.any.forEach {
			processChild(it)
			results.add(engine.get(element.returnRef))
		}

		engine.put(element.id, results)
	}

}