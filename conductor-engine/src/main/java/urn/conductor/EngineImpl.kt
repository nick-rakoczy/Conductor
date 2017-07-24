package urn.conductor

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import jdk.nashorn.api.scripting.ScriptObjectMirror
import urn.conductor.ssh.SessionProvider
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Stack
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.xml.bind.Unmarshaller

class EngineImpl(scriptEngine: ScriptEngine, override val jaxbReader: Unmarshaller, override val sessionProvider: SessionProvider) : Engine, ScriptEngine by scriptEngine {
	private val workingDirectoryStack = Stack<Path>()

	override val currentWorkingDirectory: Path
		get() = attempt { workingDirectoryStack.peek() } ?: Paths.get(".")

	override val gson: Gson by lazy {
		GsonBuilder().setPrettyPrinting().create()
	}

	private fun ScriptEngine.put(attribute: Pair<String, *>) {
		this.put(attribute.first, attribute.second)
	}

	override fun runWithContext(vararg attributes: Pair<String, *>, block: () -> Unit) {
		attributes.forEach { this.put(it) }
		block()
		attributes.forEach { this.delete(it.first) }
	}

	override fun runWithUniqueContext(vararg attributes: Pair<String, *>, block: () -> Unit) {
		attributes.map {
			it.first
		}.filter {
			this[it] != null
		}.takeIf {
			it.isNotEmpty()
		}?.let {
			error(it.joinToString(
					prefix = "Attributes already defined [",
					postfix = "]. Nested Contexts not allowed.",
					separator = ", ")
			)
		}

		this.runWithContext(*attributes) {
			block()
		}
	}

	override fun delete(name: String) {
		this.context.removeAttribute(name, ScriptContext.ENGINE_SCOPE)
	}

	override fun interpolate(expression: String) = StringBuilder().apply {
		fun String.evalAndAppend() = this.let(this@EngineImpl::eval).let(this@apply::append)

		val OPEN_MARKER = "{{"
		val CLOSE_MARKER = "}}"
		val ESCAPED_OPEN_MARKER = "\\$OPEN_MARKER"

		var left = expression

		while (left.isNotEmpty()) {
			when {
				left.startsWith(OPEN_MARKER) -> {
					left.substring(2).substringBefore(CLOSE_MARKER).evalAndAppend()
					left = left.substringAfter(CLOSE_MARKER)
				}
				left.startsWith(ESCAPED_OPEN_MARKER) -> {
					this.append(OPEN_MARKER)
					left = left.substringAfter(OPEN_MARKER)
				}
				else -> {
					this.append(left[0])
					left = left.substring(1)
				}
			}
		}
	}.toString()

	override fun getObjectMirror(name: String) = this[name] as? ScriptObjectMirror?

	override fun pushWorkingDirectory(path: Path) {
		path.toAbsolutePath().normalize().let(workingDirectoryStack::push)
	}

	override fun popWorkingDirectory() {
		workingDirectoryStack.pop()
	}

	override fun getPath(name: String): Path = currentWorkingDirectory
			.resolve(name)
			.toAbsolutePath()
			.normalize()
}