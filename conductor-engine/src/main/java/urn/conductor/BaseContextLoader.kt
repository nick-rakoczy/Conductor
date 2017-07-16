package urn.conductor

class BaseContextLoader : ScriptPreloader(BaseContextLoader::class.java) {
	init {
		priority = 0
		
		addFile("base-context.js")
	}
}