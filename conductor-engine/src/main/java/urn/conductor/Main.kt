package urn.conductor

import org.apache.logging.log4j.LogManager
import urn.conductor.ssh.SessionProviderImpl
import java.nio.file.Files
import java.nio.file.Paths
import javax.script.ScriptEngineManager

class Main(private val args: Array<String>) {
	private val log by lazy {
		LogManager.getLogger("Main")
	}

	private val options by lazy {
		HashMap<String, Any>().apply {
			this["file"] = "./plan.xml"
			this["plugins"] = "./plugins"

			args.forEach {
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
	}

	private val engineFactory by lazy {
		InternalEngineImpl(options["plugins"] as String)
	}

	private val jaxbContext by lazy {
		engineFactory.createJaxbContext()
	}

	private val jaxbReader by lazy {
		jaxbContext.createUnmarshaller()
	}

	private val scriptEngine by lazy {
		ScriptEngineManager().getEngineByName("javascript")!!
	}

	private val engine by lazy {
		EngineImpl(scriptEngine, jaxbReader, SessionProviderImpl) as Engine
	}

	private val filePath by lazy {
		options["file"].let { Paths.get(it as String) }
	}

	private val plan by lazy {
		filePath.let(Files::newBufferedReader).use(jaxbReader::unmarshal)
	}

	init {
		if (plan == null) {
			error("Unable to parse plan")
		}

		scriptEngine["options"] = options
		scriptEngine["sessionProvider"] = SessionProviderImpl
		scriptEngine["defaults"] = HashMap<String, String>()

		engine.pushWorkingDirectory(filePath.parent)

		engineFactory.executePreloaders(engine)

		log.info("Processing Plan...")

		processElement(plan)
	}

	fun processElement(element: Any) {
		engineFactory.runHandlerFor(element, engine, this::processElement)
	}

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			Main(args)
		}
	}
}