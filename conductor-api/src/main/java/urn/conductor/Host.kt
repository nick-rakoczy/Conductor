package urn.conductor

data class Host(val name: String,
				val ipAddress: String,
				val tags: Set<String>,
				val sshPort: Int)