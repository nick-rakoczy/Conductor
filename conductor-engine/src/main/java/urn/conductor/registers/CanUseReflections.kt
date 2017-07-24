package urn.conductor.registers

import org.reflections.Reflections

interface CanUseReflections {
	fun injectReflections(reflections: Reflections)
}