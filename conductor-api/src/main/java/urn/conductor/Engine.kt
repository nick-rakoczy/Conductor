package urn.conductor

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jdk.nashorn.api.scripting.ScriptObjectMirror
import java.nio.file.Path
import java.nio.file.Paths
import java.util.LinkedList
import java.util.Queue
import java.util.Stack
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.SimpleBindings
import javax.xml.bind.Unmarshaller
import kotlin.reflect.KClass

class Engine(
		private val internalScriptEngine: ScriptEngine,
		val jaxbReader: Unmarshaller
) : ScriptEngine by internalScriptEngine {
	private val gson = GsonBuilder().setPrettyPrinting().create()
	private val contextStack = Stack<String>()

	override fun toString(): String {
		return this.getBindings(ScriptContext.ENGINE_SCOPE)
				.let(gson::toJson)
	}

	fun delete(name: String) {
		this.context.removeAttribute(name, ScriptContext.ENGINE_SCOPE)
	}

	fun pushContext() {
		this.getBindings(ScriptContext.ENGINE_SCOPE)
				.let(gson::toJson)
				.let(contextStack::push)
	}

	fun popContext() {
		contextStack.pop().let {
			gson.fromJson(it, SimpleBindings::class.java)
		}.let {
			this.setBindings(it, ScriptContext.ENGINE_SCOPE)
		}
	}

	fun <T> nestContext(block: () -> T): T {
		pushContext()
		return block().also {
			popContext()
		}
	}

	fun interpolate(expr: String): String = StringBuilder().apply {
		var left = expr
		while (left.isNotEmpty()) {
			when {
				left.startsWith("{{") -> {
					val e = left.substring(2).substringBefore("}}")
					this.append(eval(e))
					left = left.substringAfter("}}")
				}
				else -> this.append(left[0]).also {
					left = left.substring(1)
				}
			}
		}
	}.let(StringBuilder::toString)

	fun getObjectMirror(name: String) = this[name] as ScriptObjectMirror?

	private val workingDirectoryStack = Stack<Path>()

	fun pushWorkingDirectory(path: Path) {
		workingDirectoryStack.push(path.toAbsolutePath().normalize())
	}

	fun popWorkingDirectory() {
		workingDirectoryStack.pop()
	}

	val currentWorkingDirectory: Path get() = attempt { workingDirectoryStack.peek() } ?: Paths.get(".")

	fun getPath(name: String): Path {
		return currentWorkingDirectory.resolve(name).toAbsolutePath().normalize()
	}

	companion object {
		fun <T> attempt(block: () -> T): T? {
			try {
				return block()
			}
			catch (e: Exception) {
				return null
			}
		}
	}
}