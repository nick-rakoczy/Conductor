package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.SetPermissions

class SetPermissionsHandler : TransportComplexElementHandler<SetPermissions> {
	override fun getHostRef(element: SetPermissions): String = element.hostRef
	override fun getIdentityRef(element: SetPermissions): String = element.identityRef

	override val handles: Class<SetPermissions>
		get() = SetPermissions::class.java


	override fun process(element: SetPermissions, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		transport.useSftpChannel {
			if (element.owner != null) {
				transport.getUserId(element.owner)?.let {
					this.chown(it, element.path)
				}
			}

			if (element.group != null) {
				transport.getGroupId(element.group)?.let {
					this.chgrp(it, element.path)
				}
			}

			if (element.mode != null) {
				this.chmod(element.mode.toInt(8), element.path)
			}
		}
	}
}