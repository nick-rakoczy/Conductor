package urn.conductor.ssh

import com.jcraft.jsch.HostKey

enum class HostKeyType(val hostKeyType: Int) {
	SSHDSS(HostKey.SSHDSS),
	SSHRSA(HostKey.SSHRSA),
	ECDSA256(HostKey.ECDSA256),
	ECDSA384(HostKey.ECDSA384),
	ECDSA521(HostKey.ECDSA521)
}