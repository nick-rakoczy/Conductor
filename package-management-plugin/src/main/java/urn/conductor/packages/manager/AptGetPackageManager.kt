package urn.conductor.packages.manager

import urn.conductor.packages.PackageManagementSystems
import urn.conductor.packages.PackageManager
import urn.conductor.packages.PackageManagment
import urn.conductor.ssh.HostTransport
import urn.conductor.ssh.execute

@PackageManager(PackageManagementSystems.APTGET)
class AptGetPackageManager : PackageManagment {
	private val HAS_RUN_UPDATE = AptGetPackageManager::class.java.name + ".hasRunUpdate"

	override fun install(transport: HostTransport, name: String, version: String) {
		if (!transport.properties.containsKey(HAS_RUN_UPDATE)) {
			transport.execute("apt-get update")
			transport.properties[HAS_RUN_UPDATE] = true
		}
		if (getInstalledVersion(transport, name) != version) {
			transport.execute("apt-get install $name=$version -y")
		}
	}

	override fun remove(transport: HostTransport, name: String) {
		transport.execute("apt-get remove $name")
	}

	override fun getInstalledVersion(transport: HostTransport, name: String): String? {
		return transport.execute("dpkg -s $name").split("\n").filter {
			it.startsWith("Version: ")
		}.map {
			it.substringAfter("Version: ")
		}.firstOrNull()
	}
}