package urn.conductor.core

import urn.conductor.stdlib.xml.InvokeMacro


class InvokeMacroHandler : urn.conductor.ComplexElementHandler<InvokeMacro> {
	override val handles: Class<InvokeMacro>
		get() = InvokeMacro::class.java

	override fun process(element: InvokeMacro, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		val macroName = element.ref
		val macroBody = engine.get(macroName) as Runnable
		macroBody.run()
	}
}