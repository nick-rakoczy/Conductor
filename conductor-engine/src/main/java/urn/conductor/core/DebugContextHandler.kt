package urn.conductor.core

class DebugContextHandler : urn.conductor.ElementHandler<urn.conductor.stdlib.xml.DebugContext> {
	override fun process(element: urn.conductor.stdlib.xml.DebugContext, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		println(engine.toString())
	}

	override val handles: Class<urn.conductor.stdlib.xml.DebugContext>
		get() = urn.conductor.stdlib.xml.DebugContext::class.java
}