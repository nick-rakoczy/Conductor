package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.stdlib.xml.Mkdir

class MkdirHandler : AbstractTransportElementHandler<Mkdir>(Mkdir::getHostRef, Mkdir::getIdentityRef) {
	override val handles: Class<Mkdir>
		get() = Mkdir::class.java

	override fun process(element: Mkdir, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		transport.useSftpChannel {
			this.mkdir(element.path)
		}
	}
}