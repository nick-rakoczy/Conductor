package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.NeverFail

class NeverFailHandler : ElementHandler<NeverFail> {
	private val logger = LogManager.getLogger()

	override val handles: Class<urn.conductor.stdlib.xml.NeverFail>
		get() = urn.conductor.stdlib.xml.NeverFail::class.java

	override fun process(element: urn.conductor.stdlib.xml.NeverFail, engine: Engine, processChild: (Any) -> Unit) {
		try {
			element.any.forEach(processChild)
		}
		catch (e: Exception) {
			when (element.mode) {
				"silent" -> let {}
				"logged" -> logger.warn(e)
				else -> error("invalid state: ${element.mode}")
			}
		}
	}
}