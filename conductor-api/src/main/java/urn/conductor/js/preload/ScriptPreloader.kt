package urn.conductor.js.preload

import urn.conductor.Engine
import urn.conductor.Preloader
import java.io.InputStream

interface ScriptPreloader {
	fun configure(handler: ScriptPreloadHandler)
}
