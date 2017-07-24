package urn.conductor.core

import org.apache.logging.log4j.LogManager

class ScriptHandler : urn.conductor.ComplexElementHandler<urn.conductor.stdlib.xml.Script> {
	private val logger = LogManager.getLogger()

	override fun process(element: urn.conductor.stdlib.xml.Script, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		element.value.orEmpty().let {
			engine.eval(it)
		}
	}

	override val handles: Class<urn.conductor.stdlib.xml.Script>
		get() = urn.conductor.stdlib.xml.Script::class.java
}