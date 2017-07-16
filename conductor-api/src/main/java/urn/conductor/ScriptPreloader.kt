package urn.conductor

import java.io.InputStream

open class ScriptPreloader(private val type: Class<out ScriptPreloader>) : Preloader {
	override var priority: Int = 100

	private fun getStream(file: String): InputStream {
		return type.getResourceAsStream(file)
	}

	private val filesToLoad = ArrayList<String>()

	fun addFile(name: String) {
		filesToLoad.add(name)
	}

	override fun configure(engine: Engine) {
		filesToLoad.forEach {
			getStream(it).use {
				engine.loadStream(it)
			}
		}
	}
}