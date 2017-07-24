package urn.conductor

interface Identity {
	val username: String
	val password: String?
	val publicKey: String?
	val privateKey: String?
}
