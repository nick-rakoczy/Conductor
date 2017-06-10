package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Container
import urn.conductor.stdlib.xml.Identity
import javax.xml.namespace.QName

class IdentityHandler : ElementHandler<Identity> {
	override fun process(element: Identity, engine: Engine, processChild: (Any) -> Unit) {
		element.run {
			urn.conductor.Identity(
					username = this.username,
					publicKey = this.sshPublicKey,
					privateKey = this.sshPrivateKey
			)
		}.let {
			engine.put(element.`as`, it)
		}
	}

	override val handles: Class<Identity>
		get() = Identity::class.java
}