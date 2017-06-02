package urn.conductor.flow

class IfHandler : urn.conductor.AttributeHandler {
	override val priority: Int
		get() = 100

	override val handles: javax.xml.namespace.QName
		get() = javax.xml.namespace.QName("urn:conductor:condition", "if")

	override fun process(element: Any, attribute: String, engine: urn.conductor.Engine, proceed: () -> Unit) {
		if (engine.eval(attribute).toString().toBoolean()) {
			proceed()
		}
	}

}