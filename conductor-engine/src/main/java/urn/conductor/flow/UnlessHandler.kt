package urn.conductor.flow

import org.apache.logging.log4j.LogManager
import urn.conductor.AttributeHandler
import urn.conductor.Engine
import javax.xml.namespace.QName

class UnlessHandler : AttributeHandler {
	private val logger = LogManager.getLogger()

	override val priority: Int
		get() = 100

	override val handles: QName
		get() = QName("urn:conductor:flow", "unless")

	override fun process(element: Any, attribute: String, engine: Engine, proceed: () -> Unit) {
		if (!engine.eval(attribute).toString().toBoolean()) {
			proceed()
		}
	}

}