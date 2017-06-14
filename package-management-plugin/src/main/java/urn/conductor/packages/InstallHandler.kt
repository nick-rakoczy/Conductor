package urn.conductor.packages

import urn.conductor.Engine
import urn.conductor.packages.xml.Install
import urn.conductor.ssh.AbstractTransportElementHandler
import urn.conductor.ssh.HostTransport

class InstallHandler : AbstractTransportElementHandler<Install>(Install::getHostRef, Install::getIdentityRef) {
	override val handles: Class<Install>
		get() = Install::class.java

	override fun process(element: Install, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		PackageManagementSystems.getPackageManager(transport)
				.install(transport, element.name, element.version)
	}
}