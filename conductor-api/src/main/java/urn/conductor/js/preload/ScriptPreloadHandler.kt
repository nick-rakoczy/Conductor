package urn.conductor.js.preload

interface ScriptPreloadHandler {
	var priority: Int

	fun addFile(name: String, friendlyName: String = name)
}