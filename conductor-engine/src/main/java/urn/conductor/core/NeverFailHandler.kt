package urn.conductor.core

import org.apache.logging.log4j.LogManager
import urn.conductor.Engine
import urn.conductor.StandardComplexElementHandler
import urn.conductor.stdlib.xml.NeverFail

class NeverFailHandler : StandardComplexElementHandler<NeverFail> {
	private val logger = LogManager.getLogger()

	override val handles: Class<NeverFail>
		get() = NeverFail::class.java

	override fun process(element: NeverFail, engine: Engine, processChild: (Any) -> Unit) {
		try {
			element.any.forEach(processChild)
		}
		catch (e: Exception) {
			when (element.mode) {
				"silent" -> logger.debug("silent failure", e)
				"logged" -> logger.warn(e)
				else -> error("invalid state: ${element.mode}")
			}
		}
	}
}