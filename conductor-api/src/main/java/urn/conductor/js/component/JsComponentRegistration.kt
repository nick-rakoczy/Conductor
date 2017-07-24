package urn.conductor.js.component

import java.io.InputStream

interface JsComponentRegistration {
	fun provideInputStreams(block: (InputStream, String) -> Unit)
}