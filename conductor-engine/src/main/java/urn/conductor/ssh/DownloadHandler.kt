package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Download
import java.net.URL

class DownloadHandler : TransportComplexElementHandler<Download> {
	override fun getHostRef(element: Download): String = element.hostRef
	override fun getIdentityRef(element: Download): String = element.identityRef

	override fun process(element: Download, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		element.url.let(engine::interpolate).let(::URL).openStream().use {
			transport.useSftpChannel {
				this.put(it, element.path.let(engine::interpolate))
			}
		}
	}



	override val handles: Class<Download>
		get() = Download::class.java
}