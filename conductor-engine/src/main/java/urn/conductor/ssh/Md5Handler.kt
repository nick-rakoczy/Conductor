package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Md5

class Md5Handler : AbstractTransportElementHandler<Md5>(Md5::getHostRef, Md5::getIdentityRef) {
	override val handles: Class<Md5>
		get() = Md5::class.java

	override fun process(element: Md5, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		val paths = element.path.let(engine::interpolate)
		DigestHelper(transport).md5(paths)[paths]?.let {
			engine.put(element.id, it)
		}
	}
}