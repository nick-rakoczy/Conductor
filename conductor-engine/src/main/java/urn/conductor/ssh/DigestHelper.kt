package urn.conductor.ssh

class DigestHelper(private val transport: HostTransport) {
	fun md5(vararg paths: String): Map<String, String> {
		return transport
				.execute("openssl dgst -md5 -hex ${paths.joinToString(" ")}")
				.split("\n")
				.filter { it.isNotBlank() }
				.map { it.split("=") }
				.map { it.map { it.trim() } }
				.map { it[0] to it[1] }
				.toMap()
				.mapKeys {
					it.key.substringAfter("MD5(").substringBefore(")")
				}
				.mapValues { it.value.toLowerCase() }
	}
}