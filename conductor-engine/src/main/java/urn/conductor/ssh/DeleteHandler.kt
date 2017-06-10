package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Delete

class DeleteHandler : AbstractTransportElementHandler<Delete>(Delete::getHostRef, Delete::getIdentityRef) {
	override val handles: Class<Delete>
		get() = Delete::class.java

	override fun process(element: Delete, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		transport.useSftpChannel {
			this.rm(element.path)
		}
	}
}