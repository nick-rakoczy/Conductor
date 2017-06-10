package urn.conductor.ssh

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.SessionManager
import urn.conductor.stdlib.xml.DisableHostKeyChecking

class DisableHostKeyCheckingHandler : ElementHandler<DisableHostKeyChecking> {
	override val handles: Class<DisableHostKeyChecking>
		get() = DisableHostKeyChecking::class.java

	override fun process(element: DisableHostKeyChecking, engine: Engine, processChild: (Any) -> Unit) {
		SessionManager.hostKeyChecking = false
	}
}