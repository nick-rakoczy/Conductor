package urn.conductor

import java.io.InputStream
import java.io.InputStreamReader

interface Preloader {
	val priority: Int
	
	val friendlyName: String

	fun configure(engine: Engine)

	fun Engine.loadStream(inputStream: InputStream) {
		InputStreamReader(inputStream).use {
			this.eval(it)
		}
	}
}