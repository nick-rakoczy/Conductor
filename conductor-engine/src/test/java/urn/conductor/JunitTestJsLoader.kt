package urn.conductor

class JunitTestJsLoader : Preloader {
	override val friendlyName: String
		get() = "JUnit Loader"
	override val priority: Int get() = 1

	override fun configure(engine: Engine) {
		this.javaClass.getResourceAsStream("/junit.js").use {
			engine.loadStream(it)
		}
	}
}