package urn.conductor.registers

import org.reflections.Reflections
import urn.conductor.CanUseReflections
import urn.conductor.ComponentRegistration
import urn.conductor.Engine
import urn.conductor.Preloader
import urn.conductor.js.preload.ScriptPreloadHandler
import urn.conductor.js.preload.ScriptPreloader
import java.io.InputStream

class ReflectionsJsPreloaderRegistration : ComponentRegistration, CanUseReflections {
	inner class ScriptPreloadHandlerImpl(val type: Class<out ScriptPreloader>) : ScriptPreloadHandler {
		override var priority: Int = 100

		private val filesToLoad = ArrayList<String>()

		override fun addFile(name: String) {
			filesToLoad.add(name)
		}

		fun getAsPreloader() = object : Preloader {
			override val priority: Int
				get() = this@ScriptPreloadHandlerImpl.priority

			private fun getStream(file: String): InputStream = type.getResourceAsStream(file)

			override fun configure(engine: Engine) {
				filesToLoad.forEach {
					getStream(it).use {
						engine.loadStream(it)
					}
				}
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
		}.mapTo(preloaders) {
			val handler = ScriptPreloadHandlerImpl(it.javaClass)
			it.configure(handler)
			handler.getAsPreloader()
		}
	}

	override fun getPreloaders(): List<Preloader> = preloaders
}
