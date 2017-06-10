package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Symlink

class SymlinkHandler : AbstractTransportElementHandler<Symlink>(Symlink::getHostRef, Symlink::getIdentityRef) {
	override val handles: Class<Symlink>
		get() = Symlink::class.java


	override fun process(element: Symlink, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		transport.useSftpChannel {
			this.symlink(element.target, element.path)
		}
	}
}