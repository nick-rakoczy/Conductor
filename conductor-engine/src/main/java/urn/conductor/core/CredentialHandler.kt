package urn.conductor.core

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Credential
import java.util.UUID

class CredentialHandler : ComplexElementHandler<Credential> {
	override val handles: Class<Credential>
		get() = Credential::class.java

	private fun Credential.toVaultRecord(): Map<String, String> = mapOf(
			"uuid" to this.uuid.let { UUID.fromString(it) }.toString().replace("-", "").toUpperCase(),
			"username" to this.username,
			"password" to this.password,
			"name" to this.name,
			"notes" to this.notes,
			"url" to this.url
	)

	override fun process(element: Credential, engine: Engine, processChild: (Any) -> Unit) {
		engine.put(element.id.let(engine::interpolate), element.toVaultRecord())
	}
}