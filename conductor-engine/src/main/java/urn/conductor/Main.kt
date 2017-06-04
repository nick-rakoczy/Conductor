package urn.conductor

import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import java.io.InputStreamReader

import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.script.ScriptEngineManager
import javax.xml.bind.JAXBContext
import javax.xml.namespace.QName

class Main(private val arguments: Array<String>) {
	private val log = LogManager.getLogger("Main")

	private val options = HashMap<String, Any>().apply {
		this["file"] = "./plan.xml"
		this["plugins"] = "./plugins"

		arguments.forEach {
			it.split("=").let {
				val key = attempt { it[0] } ?: error("Impossible null key")
				val values = attempt { it[1].split(",") }

				when {
					values == null -> this[key] = true
					values.isEmpty() -> this[key] = true
					values.size == 1 -> this[key] = values.single()
					values.size >= 2 -> this[key] = values.toList()
				}
			}
		}
	}

	private val plugin = object {
		val paths = HashSet<Path>()
		val classLoaders = HashSet<ClassLoader>()
		val attributeHandlers = HashMap<QName, AttributeHandler>()
		val elementHandlers = ElementHandlerMap()

		val jaxbInitializationArray: Array<Class<*>>
			get() = this.elementHandlers.keys.toTypedArray()

		init {
			log.info("Scanning for plugins...")

			options["plugins"].let {
				attempt {
					Files.newDirectoryStream(Paths.get(it as String))
				}
			}?.use {
				it.filterTo(paths) {
					it.fileName.toString().endsWith(".jar")
				}
			}

			paths.attemptMap {
				it.toAbsolutePath().toUri().toURL().singletonArray
			}.mapTo(classLoaders, ::URLClassLoader)

			classLoaders.add(Main::class.java.classLoader)

			log.info("Found ${classLoaders.size} packages...")

			classLoaders.filterIsInstance(URLClassLoader::class.java).map {
				it.urLs.singleOrNull()?.toString()
			}.filterNotNull().forEach(log::debug)

			val reflectionsConfig = ConfigurationBuilder.build(*classLoaders.toTypedArray(), SubTypesScanner())
			val reflections = Reflections(reflectionsConfig)

			reflections.getSubTypesOf(AttributeHandler::class.java).attemptMap {
				it.newInstance()
			}.forEach {
				attributeHandlers[it.handles] = it
			}

			log.info("Found ${attributeHandlers.size} attribute handlers...")

			attributeHandlers.keys.map {
				"{${it.namespaceURI}}:${it.localPart}"
			}.sorted().forEach(log::debug)

			reflections.getSubTypesOf(ElementHandler::class.java).attemptMap {
				it.newInstance()
			}.forEach {
				elementHandlers[it.handles] = it
			}

			log.info("Found ${elementHandlers.size} element handlers...")

			elementHandlers.keys.map {
				"{${it.xmlNamespace}}:${it.xmlElementName}"
			}.sorted().forEach(log::debug)
		}
	}

	val jaxbContext = JAXBContext.newInstance(*this@Main.plugin.jaxbInitializationArray)
	val jaxbReader = jaxbContext.createUnmarshaller()

	val filePath = Paths.get(options["file"] as String)

	private val plan = Files.newBufferedReader(filePath).use {
		jaxbReader.unmarshal(it)
	}

	private val scriptEngine = ScriptEngineManager().getEngineByName("javascript")!!

	private val engine = Engine(scriptEngine, jaxbReader)

	init {
		if (plan == null) {
			error("Unable to parse plan")
		}

		scriptEngine["options"] = options

		this.javaClass.getResourceAsStream("base-context.js").use {
			InputStreamReader(it).use {
				scriptEngine.eval(it)
			}
		}

		engine.pushWorkingDirectory(filePath.parent)

		log.info("Processing Plan...")
		processElement(plan)
	}

	fun processElement(element: Any) {
		val type = element.javaClass
		val elementHandler = plugin.elementHandlers[type]

		@Suppress("UNCHECKED_CAST")
		val elementAttributeHandler = elementHandler as? CustomAttributeHandler<Any> ?: AutoAttributeHandler

		val attributes = elementAttributeHandler.getAttributes(element)

		val attributeOrder = attributes
				.map { plugin.attributeHandlers.get(it.key) }
				.filterNotNull()
				.sortedBy { it.priority }
				.map { it.handles }
				.toMutableList()

		fun processNextAttribute() {
			if (attributeOrder.isNotEmpty()) {
				val attributeName = attributeOrder.removeAt(0)
				val attributeHandler = plugin.attributeHandlers[attributeName] ?: error("Invalid handler")
				val attributeValue = attributes[attributeName] ?: ""
				attributeHandler.process(element, attributeValue, engine, ::processNextAttribute)
			} else {
				plugin.elementHandlers[element.javaClass]
						?.process(element, engine, this::processElement)
						?: error("No handler found for element: ${element.javaClass.name}")
			}
		}

		processNextAttribute()
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			Main(args)
		}
	}
}