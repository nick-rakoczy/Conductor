package urn.conductor.aws.route53

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder
import com.amazonaws.services.route53.model.CreateHostedZoneRequest
import com.amazonaws.services.route53.model.ListHostedZonesByNameRequest
import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.Zone
import urn.conductor.aws.xml.ZonePrivacy

class ZoneHandler : ComplexElementHandler<Zone> {
	override val handles: Class<Zone>
		get() = Zone::class.java

	override fun process(element: Zone, engine: Engine, processChild: (Any) -> Unit) {
		val useMock = engine.mockEnabled()
		val credentials = engine[element.awsCredentialRef] as? AWSStaticCredentialsProvider ?: error("Invalid credential provider")
		val client = if (useMock) {
			AmazonRoute53ClientBuilder.standard()
					.withCredentials(credentials)
					.withRegion(element.region.value())
					.build()
		} else {
			MockRoute53Client()
		}

		val privateZone = element.privacy == ZonePrivacy.PRIVATE

		val hostedZoneId = client.listHostedZonesByName(ListHostedZonesByNameRequest().apply {
			this.dnsName = element.name
		}).hostedZones.filter {
			it.name == element.name && it.config.privateZone == privateZone
		}.singleOrNull()?.id ?: client.createZone(element.name, privateZone, element.region.value())

		engine.runWithUniqueContext(currentRoute53Client to client, currentRoute53Zone to hostedZoneId) {
			element.any.forEach(processChild)
		}
	}

	fun AmazonRoute53.createZone(name: String, private: Boolean, region: String) = this.createHostedZone(CreateHostedZoneRequest().apply {
		TODO()
	}).hostedZone.id

	companion object {
		val mockMarkerField = "useAwsMockImplementations"
		val currentRoute53Zone = "currentRoute53Zone"
		val currentRoute53Client = "currentRoute53Client"

		private fun Engine.mockEnabled() = this[mockMarkerField] as? Boolean ?: false
	}

}