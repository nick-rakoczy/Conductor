package urn.conductor.packages

import urn.conductor.Engine
import urn.conductor.packages.xml.Install
import urn.conductor.ssh.HostTransport
import urn.conductor.ssh.TransportComplexElementHandler

class InstallHandler : TransportComplexElementHandler<Install> {
	override fun getHostRef(element: Install): String = element.hostRef
	override fun getIdentityRef(element: Install): String = element.identityRef

	override val handles: Class<Install>
		get() = Install::class.java

	override fun process(element: Install, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		PackageManagementSystems.getPackageManager(transport)
				.install(transport, element.name, element.version)
	}
}