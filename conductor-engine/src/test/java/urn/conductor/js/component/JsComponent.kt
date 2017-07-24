package urn.conductor.js.component

import java.io.InputStream

@Suppress("unused") // Used by conductor
class JsComponent : JsComponentRegistration {
	override fun provideInputStreams(block: (InputStream, String) -> Unit) {
		val stream = this.javaClass.getResourceAsStream("component.js")
		block(stream, "JsComponent Test Loader")
	}
}