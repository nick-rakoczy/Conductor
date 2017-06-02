package urn.conductor.core

class ScriptHandler : urn.conductor.ElementHandler<urn.conductor.stdlib.xml.Script> {
	override fun process(element: urn.conductor.stdlib.xml.Script, engine: urn.conductor.Engine, processChild: (Any) -> Unit) {
		element.value.orEmpty().let {
			engine.eval(it)
		}
	}

	override fun getAttributes(element: urn.conductor.stdlib.xml.Script): Map<javax.xml.namespace.QName, String> = element.otherAttributes

	override val handles: Class<urn.conductor.stdlib.xml.Script>
		get() = urn.conductor.stdlib.xml.Script::class.java
}