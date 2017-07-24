package urn.conductor.ssh

import urn.conductor.Engine
import urn.conductor.stdlib.xml.DownloadGav
import urn.conductor.stdlib.xml.Metadata
import java.io.InputStreamReader
import java.net.URL

class DownloadGavHandler : TransportComplexElementHandler<DownloadGav> {
	override fun getHostRef(element: DownloadGav): String = element.hostRef
	override fun getIdentityRef(element: DownloadGav): String = element.identityRef

	override val handles: Class<DownloadGav>
		get() = DownloadGav::class.java

	override fun process(element: DownloadGav, engine: Engine, processChild: (Any) -> Unit, transport: HostTransport) {
		val baseUrl = element.baseUrl.let(engine::interpolate).trimEnd('/')
		val groupPath = element.groupId.let(engine::interpolate).replace('.', '/')
		val artifact = element.artifactId.let(engine::interpolate)
		val version = element.version.let(engine::interpolate)
		val packagePath = "$baseUrl/$groupPath/$artifact/$version"
		val classifier = element.classifier?.let(engine::interpolate)
		val packaging = element.packaging.let(engine::interpolate)

		val url = if (element.isSnapshot && element.version.endsWith("-SNAPSHOT")) {
			val metadataUrl = element.run { "$packagePath/maven-metadata.xml" }
			val metadata: Metadata = metadataUrl.let(::URL).openStream().use {
				InputStreamReader(it).use {
					engine.jaxbReader.unmarshal(it) as Metadata
				}
			}
			metadata.versioning.snapshotVersions.snapshotVersion.find {
				it.extension == element.packaging
			}.let {
				"$packagePath/$artifact-$it".let {
					if (classifier != null) {
						"$it-$classifier"
					} else {
						it
					}
				}.let {
					"$it.$packaging"
				}

			}
		} else {
			element.run {
				"$packagePath/$artifact-$version".let {
					if (classifier != null) {
						"$it-$classifier"
					} else {
						it
					}
				}.let {
					"$it.$packaging"
				}
			}
		}

		url.let(engine::interpolate).let(::URL).openStream().use {
			transport.useSftpChannel {
				this.put(it, element.path.let(engine::interpolate))
			}
		}
	}
}