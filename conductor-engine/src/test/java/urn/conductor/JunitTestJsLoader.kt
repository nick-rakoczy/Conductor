package urn.conductor

class JunitTestJsLoader : Preloader {
	override val priority: Int get() = 0

	override fun configure(engine: Engine) {
		this.javaClass.getResourceAsStream("/junit.js").use {
			engine.loadStream(it)
		}
	}
}