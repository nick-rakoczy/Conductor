package urn.conductor.core

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Vault
import java.nio.file.Files
import java.nio.file.Paths

class VaultHandler : ElementHandler<Vault> {
	override val handles: Class<urn.conductor.stdlib.xml.Vault>
		get() = urn.conductor.stdlib.xml.Vault::class.java

	override fun process(element: urn.conductor.stdlib.xml.Vault, engine: Engine, processChild: (Any) -> Unit) {
		val sourceFile = element.vaultSrc
				.let(engine::interpolate)
				.let { Paths.get(it) }
				.toFile()

		val vaultDatabase = de.slackspace.openkeepass.KeePassDatabase.getInstance(sourceFile)
		val vaultPassword = element.passwordFile
				?.let(engine::interpolate)
				?.let(engine::getPath)
				?.let(Files::readAllBytes)
				?.toString(Charsets.UTF_8)
		val vaultKeyFile = element.keyFile
				?.let(engine::interpolate)
				?.let(engine::getPath)
				?.toFile()

		val vault = when {
			vaultPassword != null && vaultKeyFile == null -> vaultDatabase.openDatabase(vaultPassword)
			vaultPassword == null && vaultKeyFile != null -> vaultDatabase.openDatabase(vaultKeyFile)
			vaultPassword != null && vaultKeyFile != null -> vaultDatabase.openDatabase(vaultPassword, vaultKeyFile)
			else -> error("No credentials provided for vault")
		}

		val vaultMap = HashMap<String, Any>()

		fun de.slackspace.openkeepass.domain.Entry.toVaultRecord(): Map<String, String> = mapOf(
				"uuid" to this.uuid.toString().replace("-", "").toUpperCase(),
				"username" to this.username,
				"password" to this.password,
				"name" to this.title,
				"notes" to this.notes,
				"url" to this.url
		)

		fun processRecords(parent: HashMap<String, Any>, entries: Iterable<de.slackspace.openkeepass.domain.Entry>, groups: Iterable<de.slackspace.openkeepass.domain.Group>) {
			entries.map {
				it to it.toVaultRecord()
			}.forEach { (entry, vr) ->
				parent[entry.title!!] = vr
			}

			groups.forEach {
				HashMap<String, Any>().apply {
					parent[it.name!!] = this
					processRecords(this, it.entries, it.groups)
				}
			}
		}

		processRecords(vaultMap, vault.topEntries, vault.topGroups)

		val vaultJsName = element.`as`
		engine.put(vaultJsName, vaultMap)

		println("Creating KDBX Vault named [$vaultJsName]")
	}
}