package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Container
import urn.conductor.stdlib.xml.Identity
import javax.xml.namespace.QName

class IdentityHandler : ElementHandler<Identity> {
	override fun process(element: Identity, engine: Engine, processChild: (Any) -> Unit) {
		element.let {
			urn.conductor.Identity(
					username = it.username,
					publicKey = it.sshPublicKey,
					privateKey = it.sshPrivateKey
			)
		}.let {
			engine.put(element.`as`, it)
		}
	}

	override val handles: Class<Identity>
		get() = Identity::class.java
}