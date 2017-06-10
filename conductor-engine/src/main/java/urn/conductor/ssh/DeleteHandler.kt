package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.SessionManager
import urn.conductor.stdlib.xml.Delete

class DeleteHandler : HostIdentityElementHandler<Delete>(Delete::getHostRef, Delete::getIdentityRef) {
	override val handles: Class<Delete>
		get() = Delete::class.java

	override fun process(element: Delete, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		SessionManager.getTransport(host, identity).use {
			this.remove(element.path)
		}
	}
}