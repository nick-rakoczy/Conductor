package urn.conductor.registers

import org.reflections.Reflections
import urn.conductor.ComponentRegistration
import urn.conductor.Engine
import urn.conductor.Preloader
import urn.conductor.js.preload.ScriptPreloadHandler
import urn.conductor.js.preload.ScriptPreloader
import java.io.InputStream

class ReflectionsJsPreloaderRegistration : ComponentRegistration, CanUseReflections {
	inner class ScriptPreloadHandlerImpl(val type: Class<out ScriptPreloader>) : ScriptPreloadHandler {
		override var priority: Int = 100

		private val filesToLoad = ArrayList<Pair<String, String>>()

		override fun addFile(name: String, friendlyName: String) {
			filesToLoad.add(name to friendlyName)
		}

		fun getPreloaders(): List<Preloader> = filesToLoad.map { (file, friendlyName) ->
			object : Preloader {
				override val priority: Int
					get() = this@ScriptPreloadHandlerImpl.priority
				override val friendlyName: String
					get() = friendlyName

				override fun configure(engine: Engine) {
					getStream(file).use {
						engine.loadStream(it)
					}
				}

				private fun getStream(filename: String): InputStream = type.getResourceAsStream(filename)
			}
		}
	}

	private val preloaders = ArrayList<Preloader>()

	private lateinit var reflections: Reflections

	override fun injectReflections(reflections: Reflections) {
		this.reflections = reflections
	}

	override fun init() {
		this.reflections.getSubTypesOf(ScriptPreloader::class.java).map {
			it.newInstance()
		}.flatMapTo(preloaders) {
			val handler = ScriptPreloadHandlerImpl(it.javaClass)
			it.configure(handler)
			handler.getPreloaders()
		}
	}

	override fun getPreloaders(): List<Preloader> = preloaders
}
