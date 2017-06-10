package urn.conductor.ssh

import com.jcraft.jsch.HostKey
import com.jcraft.jsch.JSch
import org.apache.logging.log4j.LogManager
import urn.conductor.Host
import urn.conductor.Identity
import java.util.Base64

object SessionProviderImpl : SessionProvider {
	private val HOST_KEY_CHECKING_PROP_NAME = "StrictHostKeyChecking"
	private val NO_KEY_PASSWORD = ByteArray(0)
	private val logger = LogManager.getLogger()
	private val jsch = JSch()
	private val base64Decoder = Base64.getDecoder()
	private val connectionCache = HashMap<Pair<Host, Identity>, HostTransport>()

	override fun registerHostKey(host: String, type: HostKeyType, key: String): String {
		val keyType = type.hostKeyType
		val keyBytes = base64Decoder.decode(key)
		val hostKey = HostKey(host, keyType, keyBytes)

		jsch.hostKeyRepository.add(hostKey, NoninteractiveUserInfo)

		return hostKey.getFingerPrint(jsch)
	}

	override fun getTransport(host: Host, identity: Identity): HostTransport {
		val key = host to identity
		return connectionCache.getOrPut(key, { newTransport(host, identity) })
	}

	override fun newTransport(host: Host, identity: Identity): HostTransport {
		val username = identity.username
		val address = host.address
		val port = host.sshPort

		val publicKeyBytes = identity.publicKey?.toByteArray()
		val privateKeyBytes = identity.privateKey?.toByteArray()
		if (publicKeyBytes != null && privateKeyBytes != null) {
			jsch.addIdentity(username, privateKeyBytes, publicKeyBytes, NO_KEY_PASSWORD)
		}

		return jsch.getSession(username, address, port).apply {
			if (!jsch.hostKeyRepository.hostKey.any { it.host == address }) {
				logger.info("No host key data provided for $host, bypassing host key verification.")
				this.setConfig(HOST_KEY_CHECKING_PROP_NAME, "no")
			}

			val password = identity.password
			if (password != null) {
				this.setPassword(password)
			}

			logger.info("Creating SSH Session for $host")

			this.connect()
		}.let(::HostTransportImpl)
	}

}