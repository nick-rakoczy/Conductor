package urn.conductor.jsc

import urn.conductor.JsComponentRegistration
import java.io.InputStream

@Suppress("unused") // Used by conductor
class JsComponent : JsComponentRegistration() {
	override fun provideInputStreams(block: (InputStream) -> Unit) {
		this.javaClass.getResourceAsStream("component.js").let(block)
	}
}