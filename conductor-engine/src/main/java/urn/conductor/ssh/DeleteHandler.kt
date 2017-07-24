package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Delete

class DeleteHandler : TransportComplexElementHandler<Delete> {
	override fun getHostRef(element: Delete): String = element.hostRef
	override fun getIdentityRef(element: Delete): String = element.identityRef

	override val handles: Class<Delete>
		get() = Delete::class.java

	override fun process(element: Delete, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		transport.useSftpChannel {
			this.rm(element.path.let(engine::interpolate))
		}
	}
}