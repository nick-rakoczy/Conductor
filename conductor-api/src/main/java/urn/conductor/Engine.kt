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
import javax.script.SimpleBindings
import javax.xml.bind.Unmarshaller

interface Engine : ScriptEngine {
	val currentWorkingDirectory: Path
	val gson: Gson
	val jaxbReader: Unmarshaller
	val sessionProvider: SessionProvider

	fun runWithContext(vararg attributes: Pair<String, *>, block: () -> Unit)
	fun runWithUniqueContext(vararg attributes: Pair<String, *>, block: () -> Unit)
	fun delete(name: String)
	fun interpolate(expression: String): String

	fun getObjectMirror(name: String): ScriptObjectMirror?
	fun pushWorkingDirectory(path: Path)
	fun popWorkingDirectory()

	fun getPath(name: String): Path
}
