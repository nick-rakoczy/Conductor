package urn.conductor.flow

import org.apache.logging.log4j.LogManager

class IfHandler : urn.conductor.AttributeHandler {
	private val logger = LogManager.getLogger()

	override val priority: Int
		get() = 100

	override val handles: javax.xml.namespace.QName
		get() = javax.xml.namespace.QName("urn:conductor:flow", "if")

	override fun process(element: Any, attribute: String, engine: urn.conductor.Engine, proceed: () -> Unit) {
		if (engine.eval(attribute).toString().toBoolean()) {
			proceed()
		}
	}

}