package urn.conductor

import org.reflections.Reflections
import java.lang.reflect.Modifier
import javax.management.Attribute
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
	override fun getComplexElementHandlers() = reflectInstancesOfSubclasses(ElementHandler::class)
	override fun getPreloaders() = reflectInstancesOfSubclasses(Preloader::class)
	override fun getSimpleElementHandlers() = reflectInstancesOfSubclasses(SimpleElementHandler::class)
}