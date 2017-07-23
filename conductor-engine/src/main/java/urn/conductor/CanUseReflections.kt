package urn.conductor

import org.reflections.Reflections

interface CanUseReflections {
	fun injectReflections(reflections: Reflections)
}