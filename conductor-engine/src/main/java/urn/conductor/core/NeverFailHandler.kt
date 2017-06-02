package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.NeverFail

class NeverFailHandler : ElementHandler<NeverFail> {
	override val handles: Class<urn.conductor.stdlib.xml.NeverFail>
		get() = urn.conductor.stdlib.xml.NeverFail::class.java

	override fun process(element: urn.conductor.stdlib.xml.NeverFail, engine: Engine, processChild: (Any) -> Unit) {
		try {
			element.any.forEach(processChild)
		}
		catch (e: Exception) {
			when (element.mode) {
				"silent" -> let {}
				"logged" -> println(e)
				else -> error("invalid state: ${element.mode}")
			}
		}
	}
}