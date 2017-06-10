package urn.conductor.ssh

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.SessionManager
import urn.conductor.stdlib.xml.Symlink

class SymlinkHandler : HostIdentityElementHandler<Symlink>(Symlink::getHostRef, Symlink::getIdentityRef) {
	override val handles: Class<Symlink>
		get() = Symlink::class.java

	override fun process(element: Symlink, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		SessionManager.getTransport(host, identity).use {
			this.symlink(element.path, element.target)
		}
	}
}