package urn.conductor


class DefaultValuesLoader : Preloader {
	override val priority: Int
		get() = 1

	override fun configure(engine: Engine) {
		this.javaClass.getResourceAsStream("default-values.js").use {
			engine.loadStream(it)
		}
	}
}