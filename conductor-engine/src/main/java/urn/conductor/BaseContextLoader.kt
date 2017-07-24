package urn.conductor

import urn.conductor.js.preload.ScriptPreloadHandler
import urn.conductor.js.preload.ScriptPreloader

class BaseContextLoader : ScriptPreloader {
	override fun configure(handler: ScriptPreloadHandler) {
		handler.priority = 0
		handler.addFile("base-context.js")
	}

}