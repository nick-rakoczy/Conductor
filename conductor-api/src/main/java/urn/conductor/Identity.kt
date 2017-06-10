package urn.conductor

data class Identity(val username: String,
					val password: String?,
					val publicKey: String?,
					val privateKey: String?)