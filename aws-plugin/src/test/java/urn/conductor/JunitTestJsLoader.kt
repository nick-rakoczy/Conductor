package urn.conductor

import urn.conductor.js.preload.ScriptPreloadHandler
import urn.conductor.js.preload.ScriptPreloader

class JunitTestJsLoader : ScriptPreloader {
	override fun configure(handler: ScriptPreloadHandler) {
		handler.priority = 1
		handler.addFile("/junit.js", "JUnit Loader")
	}
}