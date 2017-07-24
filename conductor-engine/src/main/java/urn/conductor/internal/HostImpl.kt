package urn.conductor.internal

import urn.conductor.Host

data class HostImpl(override val name: String,
					override val address: String,
					override val tags: Set<String>,
					override val sshPort: Int) : Host