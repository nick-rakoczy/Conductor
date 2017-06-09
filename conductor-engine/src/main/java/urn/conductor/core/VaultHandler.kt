package urn.conductor.core

import de.slackspace.openkeepass.KeePassDatabase
import de.slackspace.openkeepass.domain.Entry
import de.slackspace.openkeepass.domain.Group
import org.apache.logging.log4j.LogManager
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.stdlib.xml.Vault
import java.nio.file.Files

class VaultHandler : ElementHandler<Vault> {
	private val logger = LogManager.getLogger()

	override val handles: Class<Vault>
		get() = Vault::class.java

	private fun Entry.toVaultRecord(): Map<String, String> = mapOf(
			"uuid" to this.uuid.toString().replace("-", "").toUpperCase(),
			"username" to this.username,
			"password" to this.password,
			"name" to this.title,
			"notes" to this.notes,
			"url" to this.url
	)

	private fun processRecords(parent: HashMap<String, Any>, entries: Iterable<Entry>, groups: Iterable<Group>) {
		entries.map {
			it to it.toVaultRecord()
		}.forEach { (entry, vr) ->
			parent[entry.title!!] = vr
		}

		groups.forEach {
			parent.getOrPut(it.name!!, { HashMap<String, Any>() }).let {
				@Suppress("UNCHECKED_CAST")
				it as HashMap<String, Any>
			}.apply {
				processRecords(this, it.entries, it.groups)
			}
		}
	}


	override fun process(element: Vault, engine: Engine, processChild: (Any) -> Unit) {
		val vaultJsName = element.`as`
		val vaultMap = HashMap<String, Any>()

		element.source.map {
			val sourcePath = it.src
					.let(engine::interpolate)
					.let(engine::getPath)
			val sourceFile = sourcePath.toFile()
			val vaultDatabase = KeePassDatabase.getInstance(sourceFile)
			val vaultPassword = it.passwordFile
					?.let(engine::interpolate)
					?.let(engine::getPath)
					?.let(Files::readAllBytes)
					?.toString(Charsets.UTF_8)
			val vaultKeyFile = it.keyFile
					?.let(engine::interpolate)
					?.let(engine::getPath)
					?.toFile()

			val vault = when {
				vaultPassword != null && vaultKeyFile == null -> vaultDatabase.openDatabase(vaultPassword)
				vaultPassword == null && vaultKeyFile != null -> vaultDatabase.openDatabase(vaultKeyFile)
				vaultPassword != null && vaultKeyFile != null -> vaultDatabase.openDatabase(vaultPassword, vaultKeyFile)
				else -> error("No credentials provided for vault")
			}

			processRecords(vaultMap, vault.topEntries, vault.topGroups)

			logger.info("Loaded KDBX [${sourcePath.toAbsolutePath().normalize()}] as $vaultJsName")
		}

		engine.put(vaultJsName, vaultMap)
	}
}