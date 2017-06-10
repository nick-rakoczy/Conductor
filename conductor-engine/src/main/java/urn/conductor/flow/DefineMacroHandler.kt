package urn.conductor.flow

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.DefineMacro

class DefineMacroHandler : ElementHandler<DefineMacro> {
	override val handles: Class<DefineMacro>
		get() = DefineMacro::class.java

	override fun process(element: DefineMacro, engine: Engine, processChild: (Any) -> Unit) {
		val macroName = element.id
		val macroBody = Runnable {
			element.any.forEach {
				processChild(it)
			}
		}

		engine.put(macroName, macroBody)
	}
}