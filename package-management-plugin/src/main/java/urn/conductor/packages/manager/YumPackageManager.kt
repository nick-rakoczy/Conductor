package urn.conductor.packages.manager

import urn.conductor.packages.PackageManagementSystems
import urn.conductor.packages.PackageManager
import urn.conductor.packages.PackageManagment
import urn.conductor.ssh.HostTransport
import urn.conductor.ssh.execute

@PackageManager(PackageManagementSystems.YUM)
class YumPackageManager : PackageManagment {
	private val HAS_RUN_UPDATE = YumPackageManager::class.java.name + ".hasRunUpdate"

	override fun install(transport: HostTransport, name: String, version: String) {
		if (!transport.properties.containsKey(HAS_RUN_UPDATE)) {
			transport.execute("yum makecache")
			transport.properties[HAS_RUN_UPDATE] = true
		}
		if (getInstalledVersion(transport, name) != version) {
			transport.execute("yum install $name-$version -y")
		}
	}

	override fun remove(transport: HostTransport, name: String) {
		transport.execute("yum remove $name")
	}

	override fun getInstalledVersion(transport: HostTransport, name: String): String? {
		return transport.execute("yum info $name").split("\n").filter {
			it.contains(":") && it.substringBefore(":").trim() == "Version"
		}.map {
			it.substringAfter(":").trim()
		}.firstOrNull()
	}
}