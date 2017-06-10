package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Identity

class IdentityHandler : ElementHandler<Identity> {
	override fun process(element: Identity, engine: Engine, processChild: (Any) -> Unit) {
		val user = element.username?.let(engine::interpolate) ?: error("Missing username")
		val publicKey = element.publicKeyRef?.let(engine::get) as? String
		val privateKey = element.privateKeyRef?.let(engine::get) as? String
		val password = element.password?.let(engine::interpolate)

		val ident = urn.conductor.Identity(username = user,
				privateKey = privateKey,
				publicKey = publicKey,
				password = password)
		engine.put(element.id, ident)
	}


	override val handles: Class<Identity>
		get() = Identity::class.java
}