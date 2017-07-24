package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.StandardComplexElementHandler
import urn.conductor.stdlib.xml.Script

class ScriptHandler : StandardComplexElementHandler<Script> {
	private val logger = LogManager.getLogger()

	override fun process(element: Script, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		element.value.orEmpty().let {
			engine.eval(it)
		}
	}

	override val handles: Class<Script>
		get() = Script::class.java
}