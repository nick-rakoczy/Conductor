package urn.conductor.ssh

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.SessionManager
import urn.conductor.absolutePathString
import urn.conductor.stdlib.xml.Md5
import urn.conductor.stdlib.xml.Shell
import java.nio.file.Files

class Md5Handler : HostIdentityElementHandler<Md5>(Md5::getHostRef, Md5::getIdentityRef) {
	override val handles: Class<Md5>
		get() = Md5::class.java

	override fun process(element: Md5, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		SessionManager.getTransport(host, identity).use {
			this.md5(element.path.let(engine::interpolate)).let {
				engine.put(element.id, it)
			}
		}
	}
}