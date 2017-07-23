package urn.conductor

import urn.conductor.aws.route53.ZoneHandler

class AwsUseMockImplLoader : Preloader {
	override val priority: Int get() = 10

	override fun configure(engine: Engine) {
		engine.put(ZoneHandler.mockMarkerField, true)
	}
}
