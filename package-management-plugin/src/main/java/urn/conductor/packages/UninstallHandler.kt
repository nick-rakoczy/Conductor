package urn.conductor.packages

import urn.conductor.Engine
import urn.conductor.packages.xml.Uninstall
import urn.conductor.ssh.HostTransport
import urn.conductor.ssh.TransportComplexElementHandler

class UninstallHandler : TransportComplexElementHandler<Uninstall> {
	override fun getHostRef(element: Uninstall): String = element.hostRef
	override fun getIdentityRef(element: Uninstall): String = element.identityRef

	override val handles: Class<Uninstall>
		get() = Uninstall::class.java

	override fun process(element: Uninstall, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		PackageManagementSystems.getPackageManager(transport)
				.remove(transport, element.name)
	}
}