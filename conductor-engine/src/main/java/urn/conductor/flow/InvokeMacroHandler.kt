package urn.conductor.flow

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.InvokeMacro


class InvokeMacroHandler : ElementHandler<InvokeMacro> {
	override val handles: Class<InvokeMacro>
		get() = InvokeMacro::class.java

	override fun process(element: InvokeMacro, engine: Engine, processChild: (Any) -> Unit) {
		val macroName = element.ref
		val macroBody = engine.get(macroName) as Runnable
		macroBody.run()
	}
}