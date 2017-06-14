package urn.conductor.packages

import urn.conductor.Engine
import urn.conductor.packages.xml.Uninstall
import urn.conductor.ssh.AbstractTransportElementHandler
import urn.conductor.ssh.HostTransport

class UninstallHandler : AbstractTransportElementHandler<Uninstall>(Uninstall::getHostRef, Uninstall::getIdentityRef) {
	override val handles: Class<Uninstall>
		get() = Uninstall::class.java

	override fun process(element: Uninstall, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		PackageManagementSystems.getPackageManager(transport)
				.remove(transport, element.name)
	}
}