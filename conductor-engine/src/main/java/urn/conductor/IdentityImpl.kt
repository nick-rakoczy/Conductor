package urn.conductor

data class IdentityImpl(override val username: String,
						override val password: String?,
						override val publicKey: String?,
						override val privateKey: String?) : Identity
