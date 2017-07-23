package urn.conductor

import jdk.internal.dynalink.beans.StaticClass
import jdk.nashorn.api.scripting.ScriptObjectMirror
import sun.java2d.pipe.SpanShapeRenderer
import java.io.InputStream
import java.io.InputStreamReader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.xml.namespace.QName

abstract class JsComponentRegistration : ComponentRegistration {

	override fun init() {
		val se = ScriptEngineManager().getEngineByName("javascript")!!

		se.put("component", this)

		provideInputStreams {
			InputStreamReader(it).use {
				se.eval(it)
			}
		}
	}

	private val attributeHandlers = ArrayList<AttributeHandler>()
	private val simpleElementHandlers = ArrayList<SimpleElementHandler>()
	private val complexElementHandlers = ArrayList<ElementHandler<*>>()
	private val preloaders = ArrayList<Preloader>()

	override fun getAttributeHandlers() = attributeHandlers
	override fun getSimpleElementHandlers() = simpleElementHandlers
	override fun getComplexElementHandlers() = complexElementHandlers
	override fun getPreloaders() = preloaders

	fun registerAttributeHandler(namespace: String, localName: String, priority: Int, handler: (Any, String, Engine, () -> Unit) -> Unit) {
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

	fun registerSimpleElementHandler(namespace: String, localName: String, handler: (Any, Engine) -> Unit) {
		simpleElementHandlers.add(object : SimpleElementHandler {
			override val handles: QName
				get() = QName(namespace, localName)

			override fun process(element: Any, engine: Engine) {
				handler(element, engine)
			}
		})
	}

	fun <T : Any> registerComplexElementHandler(type: StaticClass, handler: (T, Engine, (Any) -> Unit) -> Unit) {
		complexElementHandlers.add(object : ElementHandler<T> {
			override val handles: Class<T>
				get() = type.representedClass as Class<T>

			override fun process(element: T, engine: Engine, processChild: (Any) -> Unit) {
				handler(element, engine, processChild)
			}
		})
	}

	fun registerPreloader(priority: Int, preloadFunction: (Engine) -> Unit) {
		preloaders.add(object : Preloader {
			override val priority: Int
				get() = priority

			override fun configure(engine: Engine) {
				preloadFunction(engine)
			}

		})
	}

	abstract fun provideInputStreams(block: (InputStream) -> Unit)
}