package urn.conductor.jsc

import java.io.InputStream

interface JsComponentRegistration {
	fun provideInputStreams(block: (InputStream) -> Unit)
}