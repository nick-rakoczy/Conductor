package urn.conductor

import urn.conductor.stdlib.xml.Script

class JunitTestJsLoader : ScriptPreloader(JunitTestJsLoader::class.java) {
	init {
		priority = 0
		addFile("/junit.js")
	}
}