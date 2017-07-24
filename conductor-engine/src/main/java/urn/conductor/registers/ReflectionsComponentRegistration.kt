package urn.conductor.registers

import org.reflections.Reflections
import urn.conductor.AttributeHandler
import urn.conductor.CanUseReflections
import urn.conductor.ComplexElementHandler
import urn.conductor.ComponentRegistration
import urn.conductor.Preloader
import urn.conductor.SimpleElementHandler
import urn.conductor.attemptMap
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

class ReflectionsComponentRegistration : ComponentRegistration, CanUseReflections {
	private lateinit var reflections: Reflections

	override fun injectReflections(reflections: Reflections) {
		this.reflections = reflections
	}

	private fun <T : Any> reflectInstancesOfSubclasses(type: KClass<T>): List<T> = this
			.reflections
			.getSubTypesOf(type.java)
			.filter {
				it.modifiers.let { mod ->
					listOf(Modifier::isAbstract,
							Modifier::isInterface,
							Modifier::isPrivate,
							Modifier::isProtected,
							Modifier::isStatic)
							.map { it.invoke(mod) }
							.filter { it }
							.none()
				}
			}
			.attemptMap(Class<out T>::newInstance)
			.toList()

	override fun getAttributeHandlers() = reflectInstancesOfSubclasses(AttributeHandler::class)
	override fun getComplexElementHandlers() = reflectInstancesOfSubclasses(ComplexElementHandler::class)
	override fun getPreloaders() = reflectInstancesOfSubclasses(Preloader::class)
	override fun getSimpleElementHandlers() = reflectInstancesOfSubclasses(SimpleElementHandler::class)
}