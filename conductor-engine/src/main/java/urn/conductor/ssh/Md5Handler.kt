package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.Md5

class Md5Handler : TransportComplexElementHandler<Md5> {
	override fun getHostRef(element: Md5): String = element.hostRef
	override fun getIdentityRef(element: Md5): String = element.identityRef

	override val handles: Class<Md5>
		get() = Md5::class.java

	override fun process(element: Md5, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		val paths = element.path.let(engine::interpolate)
		DigestHelper(transport).md5(paths)[paths]?.let {
			engine.put(element.id, it)
		}
	}
}