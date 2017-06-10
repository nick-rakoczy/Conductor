package urn.conductor

class BaseContextLoader : Preloader {
	override val priority: Int
		get() = 0

	override fun configure(engine: Engine) {
		BaseContextLoader::class.java.getResourceAsStream("base-context.js").use {
			engine.loadStream(it)
		}
	}
}