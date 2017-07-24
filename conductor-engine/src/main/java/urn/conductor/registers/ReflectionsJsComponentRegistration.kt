package urn.conductor.registers

import jdk.internal.dynalink.beans.StaticClass
import org.reflections.Reflections
import urn.conductor.AttributeHandler
import urn.conductor.ComplexElementHandler
import urn.conductor.ComponentRegistration
import urn.conductor.Engine
import urn.conductor.Preloader
import urn.conductor.SimpleElementHandler
import urn.conductor.js.component.JsComponentRegistration
import urn.conductor.js.component.JsComponentRegistrationHandler
import java.io.InputStreamReader
import javax.script.ScriptEngineManager
import javax.xml.namespace.QName

class ReflectionsJsComponentRegistration : ComponentRegistration, JsComponentRegistrationHandler, CanUseReflections {
	private lateinit var reflections: Reflections

	override fun injectReflections(reflections: Reflections) {
		this.reflections = reflections
	}

	private var currentFileName: String? = null

	override fun init() {
		val se = ScriptEngineManager().getEngineByName("javascript")!!

		se.put("component", this as JsComponentRegistrationHandler)

		this.reflections.getSubTypesOf(JsComponentRegistration::class.java).map {
			it.newInstance()
		}.forEach {
			it.provideInputStreams { stream, name ->
				currentFileName = name
				InputStreamReader(stream).use {
					se.eval(it)
				}
				currentFileName = null
			}
		}
	}

	private val attributeHandlers = ArrayList<AttributeHandler>()
	private val simpleElementHandlers = ArrayList<SimpleElementHandler>()
	private val complexElementHandlers = ArrayList<ComplexElementHandler<*>>()
	private val preloaders = ArrayList<Preloader>()

	override fun getAttributeHandlers() = attributeHandlers
	override fun getSimpleElementHandlers() = simpleElementHandlers
	override fun getComplexElementHandlers() = complexElementHandlers
	override fun getPreloaders() = preloaders

	override fun registerAttributeHandler(namespace: String, localName: String, priority: Int, handler: (Any, String, Engine, () -> Unit) -> Unit) {
		attributeHandlers.add(object : AttributeHandler {
			override val handles: QName
				get() = QName(namespace, localName)
			override val priority: Int
				get() = priority

			override fun process(element: Any, attribute: String, engine: Engine, proceed: () -> Unit) {
				handler(element, attribute, engine, proceed)
			}
		})
	}

	override fun registerSimpleElementHandler(namespace: String, localName: String, handler: (Any, Engine) -> Unit) {
		simpleElementHandlers.add(object : SimpleElementHandler {
			override val handles: QName
				get() = QName(namespace, localName)

			override fun process(element: Any, engine: Engine) {
				handler(element, engine)
			}
		})
	}

	override fun <T : Any> registerComplexElementHandler(type: StaticClass, handler: (T, Engine, (Any) -> Unit) -> Unit) {
		complexElementHandlers.add(object : ComplexElementHandler<T> {
			override val handles: Class<T>
				@Suppress("UNCHECKED_CAST")
				get() = type.representedClass as Class<T>

			override fun process(element: T, engine: Engine, processChild: (Any) -> Unit) {
				handler(element, engine, processChild)
			}
		})
	}

	override fun registerPreloader(priority: Int, preloadFunction: (Engine) -> Unit) {
		preloaders.add(object : Preloader {
			override val priority: Int
				get() = priority

			override val friendlyName: String = currentFileName ?: error("No filename provided")

			override fun configure(engine: Engine) {
				preloadFunction(engine)
			}
		})
	}
}