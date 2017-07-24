package urn.conductor.core

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.DefineMacro

class DefineMacroHandler : ComplexElementHandler<DefineMacro> {
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