package urn.conductor.ssh

import com.jcraft.jsch.Session
import urn.conductor.Host
import urn.conductor.Identity


interface SessionProvider {
	/**
	 * Register the provided key, returns the key's fingerprint
	 */
	fun registerHostKey(host: String, type: HostKeyType, key: String): String

	/**
	 * Create a new transport that is not part of the connection cache.
	 */
	fun newTransport(host: Host, identity: Identity): HostTransport

	/**
	 * Get a transport from the connection cache,
	 * or create a new connection and add it to the cache.
	 */
	fun getTransport(host: Host, identity: Identity): HostTransport
}