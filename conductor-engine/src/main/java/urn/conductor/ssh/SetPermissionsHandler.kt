package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.Host
import urn.conductor.Identity
import urn.conductor.SessionManager
import urn.conductor.stdlib.xml.SetPermissions

class SetPermissionsHandler : HostIdentityElementHandler<SetPermissions>(SetPermissions::getHostRef, SetPermissions::getIdentityRef) {
	override val handles: Class<SetPermissions>
		get() = SetPermissions::class.java

	override fun process(element: SetPermissions, engine: Engine, processChild: (Any) -> Unit, host: Host, identity: Identity) {
		SessionManager.getTransport(host, identity).use {
			if (element.owner != null) {
				this.chown(element.path, element.owner)
			}

			if (element.group != null) {
				this.chgrp(element.path, element.group)
			}

			if (element.mode != null) {
				this.chmod(element.path, element.mode.toInt(8))
			}
		}
	}
}