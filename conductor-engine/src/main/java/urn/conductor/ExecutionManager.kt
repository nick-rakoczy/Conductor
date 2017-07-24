package urn.conductor

import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ConfigurationBuilder
import urn.conductor.registers.CanUseReflections
import urn.conductor.ssh.TransportComplexElementHandler
import java.lang.reflect.Modifier
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.bind.annotation.XmlRegistry
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSchema
import javax.xml.namespace.QName

class ExecutionManager(pluginsDir: String) {
	private val logger = LogManager.getLogger("Internal")
	private val attributeHandlers = HashMap<QName, AttributeHandler>()
	private val simpleElementHandlers = HashMap<QName, SimpleElementHandler>()
	private val complexElementHandlers = ElementHandlerMap()
	private val preloaders = ArrayList<Preloader>()
	private val objectFactories = HashSet<Class<*>>()

	val Class<*>.xmlNamespace: String?
		get() = this.getAnnotation(XmlRootElement::class.java)?.namespace?.takeIf { it != "##default" }
				?: this.`package`?.getAnnotation(XmlSchema::class.java)?.namespace

	val Class<*>.xmlElementName: String?
		get() = this.getAnnotation(XmlRootElement::class.java)?.name

	init {
		logger.info("Scanning for plugins...")

		val paths = pluginsDir.let {
			Paths.get(it)
		}.let {
			attempt {
				Files.newDirectoryStream(it)
			} ?: error("Directory stream failed")
		}.use {
			it.filter {
				it.fileName.toString().endsWith(".jar")
			}.filterNotNull()
		}

		val classloaders = paths.map {
			attempt {
				it.toAbsolutePath().toUri().toURL()
			}
		}.filterNotNull().map {
			arrayOf(it)
		}.map(::URLClassLoader)

		logger.info("Found ${classloaders.size} packages...")

		classloaders.map {
			it.urLs.singleOrNull()?.toString()
		}.filterNotNull().forEach(logger::debug)

		val reflectionsConfig = ConfigurationBuilder.build(this.javaClass.classLoader, *classloaders.toTypedArray(), SubTypesScanner(), TypeAnnotationsScanner())
		val reflections = Reflections(reflectionsConfig)

		reflections.getTypesAnnotatedWith(XmlRegistry::class.java)
				.forEach { objectFactories.add(it) }

		reflections.getSubTypesOf(ComponentRegistration::class.java).filter {
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
		}.map {
			attempt {
				it.newInstance()
			}
		}.filterNotNull().forEach {
			logger.debug("Found ComponentRegistration: ${it.javaClass.name}")

			if (it is CanUseReflections) {
				it.injectReflections(reflections)
			}

			it.init()

			it.getAttributeHandlers().forEach {
				attributeHandlers[it.handles] = it
			}
			it.getComplexElementHandlers().forEach {
				complexElementHandlers[it.handles] = it
			}
			it.getSimpleElementHandlers().forEach {
				simpleElementHandlers[it.handles] = it
			}
			it.getPreloaders().forEach {
				preloaders.add(it)
			}
		}

		fun logDetails(name: String, getCount: () -> Int, debugLines: () -> List<String>) {
			logger.info("Found ${getCount()} $name...")
			debugLines().sorted().forEach(logger::debug)
		}

		logger.info("Found ${attributeHandlers.size} attribute handlers...")
		attributeHandlers.keys
				.map(QName::toString)
				.sorted()
				.forEach(logger::debug)

		logger.info("Found ${simpleElementHandlers.size} simple element handlers...")
		simpleElementHandlers.keys
				.map(QName::toString)
				.sorted()
				.forEach(logger::debug)

		logger.info("Found ${complexElementHandlers.size} complex element handlers...")
		complexElementHandlers.keys
				.map { QName(it.xmlNamespace, it.xmlElementName).toString() }
				.sorted()
				.forEach(logger::debug)

		logger.info("Found ${preloaders.size} preloaders...")
		preloaders.sortedBy { it.priority }
				.map { "[${it.priority}] ${it.friendlyName}" }
				.forEach(logger::debug)
	}


	fun runHandlerFor(element: Any, engine: Engine, proceed: (Any) -> Unit) {
		when {
			element is JAXBElement<*> -> {
				simpleElementHandlers[element.name]?.process(element.value, engine)
						?: error("Unknown handler for simple element: ${element.name}")
			}
			else -> {
				val handler = complexElementHandlers[element.javaClass]
						?: error("Unknown handler for complex element: ${element.javaClass.name}")

				@Suppress("UNCHECKED_CAST")
				val elementAttributeHandler = handler as? CustomAttributeHandler<Any> ?: AutoAttributeHandler

				val attributes = elementAttributeHandler.getAttributes(element)

				val attributeOrder = attributes
						.map { this.attributeHandlers[it.key] }
						.filterNotNull()
						.sortedBy { it.priority }
						.map { it.handles }
						.toMutableList()

				fun processNextAttribute() {
					if (attributeOrder.isNotEmpty()) {
						val attributeName = attributeOrder.removeAt(0)
						val attributeHandler = attributeHandlers[attributeName] ?: error("No attribute handler found for: $attributeName")
						val attributeValue = attributes[attributeName] ?: ""
						attributeHandler.process(element, attributeValue, engine, ::processNextAttribute)
					} else {
						when (handler) {
							is TransportComplexElementHandler<Any> -> {
								val host = handler.getHostRef(element)
										.let(engine::interpolate)
										.let(engine::get)
										.let { it as Host }
								val identity = handler.getIdentityRef(element)
										.let(engine::interpolate)
										.let(engine::get)
										.let { it as Identity }
								val transport = engine.sessionProvider.getTransport(host, identity)
								handler.process(element, engine, proceed, transport)
							}
							is StandardComplexElementHandler<Any> -> handler.process(element, engine, proceed)
							else -> error("Unknown element handler type: ${handler.javaClass.name}")
						}
					}
				}

				processNextAttribute()
			}
		}
	}

	fun createJaxbContext() = this.objectFactories.toTypedArray().let {
		JAXBContext.newInstance(*it)
	}

	fun executePreloaders(engine: Engine) {
		this.preloaders.sortedBy {
			it.priority
		}.forEach {
			it.configure(engine)
		}
	}
}