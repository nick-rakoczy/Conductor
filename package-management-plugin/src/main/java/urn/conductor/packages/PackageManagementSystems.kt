package urn.conductor.packages

import urn.conductor.attempt
import urn.conductor.ssh.HostTransport

enum class PackageManagementSystems(val verificationCommand: String) {
	YUM("yum --version"),
	APTGET("apt-get --version");

	companion object {
		private val PMS_TYPE = PackageManagementSystems::class.java.name + ".pmsType"

		fun detect(transport: HostTransport): PackageManagementSystems? = values()
				.firstOrNull {
					attempt {
						transport.execute(path = it.verificationCommand)
						true
					} ?: false
				}

		fun verify(transport: HostTransport) {
			if (!transport.properties.containsKey(PMS_TYPE)) {
				detect(transport)?.let {
					transport.properties[PMS_TYPE] = it
				}
			}
		}

		fun getPackageManager(transport: HostTransport): PackageManagment {
			verify(transport)

			return transport.properties[PMS_TYPE]?.let {
				PackageManagment.implementations[it]
			} ?: error("Unable to find package management implementation.")
		}
	}
}