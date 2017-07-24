package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Mkdir

class MkdirHandler : TransportComplexElementHandler<Mkdir> {
	override fun getHostRef(element: Mkdir): String = element.hostRef
	override fun getIdentityRef(element: Mkdir): String = element.identityRef

	override val handles: Class<Mkdir>
		get() = Mkdir::class.java

	override fun process(element: Mkdir, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		transport.useSftpChannel {
			this.mkdir(element.path)
		}
	}
}