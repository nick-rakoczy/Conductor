package urn.conductor

data class Host(val name: String,
				val address: String,
				val tags: Set<String>,
				val sshPort: Int)