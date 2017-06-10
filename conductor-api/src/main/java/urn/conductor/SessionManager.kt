package urn.conductor

import com.jcraft.jsch.HostKey
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jcraft.jsch.UserInfo
import org.apache.logging.log4j.LogManager
import java.util.Base64

object SessionManager {
	var hostKeyChecking: Boolean = true

	private val jsch by lazy { JSch() }

	private val connectionMap = HashMap<Pair<Host, Identity>, Session>()

	private val logger = LogManager.getLogger()

	fun registerHostKey(host: String, type: String, key: String): String {
		val keyType = when (type) {
			"SSHDSS" -> HostKey.SSHDSS
			"SSHRSA" -> HostKey.SSHRSA
			"ECDSA256" -> HostKey.ECDSA256
			"ECDSA384" -> HostKey.ECDSA384
			"ECDSA521" -> HostKey.ECDSA521
			else -> error("Invalid key type defined: $type")
		}

		val keyBytes = Base64.getDecoder().decode(key)

		HostKey(host, keyType, keyBytes).run {
			jsch.hostKeyRepository.add(this, NonInteractiveUserInfo)
		}

		return jsch.hostKeyRepository.hostKey.last().getFingerPrint(jsch)
	}

	fun getTransport(host: Host, id: Identity): Session {
		return connectionMap.getOrPut(host to id, {
			createTransport(host, id)
		})
	}

	fun createTransport(host: Host, id: Identity): Session {
		val username = id.username
		val ipaddr = host.address
		val port = host.sshPort

		val privateKeyBytes = id.privateKey?.toByteArray()
		val publicKeyBytes = id.publicKey?.toByteArray()

		if (privateKeyBytes != null && publicKeyBytes != null) {
			jsch.addIdentity(username, privateKeyBytes, publicKeyBytes, ByteArray(0))
		}

		val session = jsch.getSession(username, ipaddr, port)


		if (!hostKeyChecking) {
			session.setConfig("StrictHostKeyChecking", "no")
		}

		id.password?.let(session::setPassword)
		session.connect()

		logger.info("Created SSH Session for $host")

		return session
	}

	object NonInteractiveUserInfo : UserInfo {
		override fun promptPassphrase(p0: String?): Boolean {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun getPassphrase(): String {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun getPassword(): String {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun promptYesNo(p0: String?): Boolean {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun showMessage(p0: String?) {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun promptPassword(p0: String?): Boolean {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}
	}
}