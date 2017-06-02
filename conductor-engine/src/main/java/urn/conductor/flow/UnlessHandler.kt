package urn.conductor.flow

import urn.conductor.AttributeHandler
import urn.conductor.Engine
import javax.xml.namespace.QName

class UnlessHandler : AttributeHandler {
	override val priority: Int
		get() = 100

	override val handles: QName
		get() = QName("urn:conductor:condition", "unless")

	override fun process(element: Any, attribute: String, engine: Engine, proceed: () -> Unit) {
		if (!engine.eval(attribute).toString().toBoolean()) {
			proceed()
		}
	}

}