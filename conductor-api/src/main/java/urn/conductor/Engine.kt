package urn.conductor

import com.google.gson.Gson
import jdk.nashorn.api.scripting.ScriptObjectMirror
import urn.conductor.ssh.SessionProvider
import java.nio.file.Path
import javax.script.ScriptEngine
import javax.xml.bind.Unmarshaller

interface Engine : ScriptEngine {
	val currentWorkingDirectory: Path
	val gson: Gson
	val jaxbReader: Unmarshaller
	val sessionProvider: SessionProvider

	fun <T> runWithContext(vararg attributes: Pair<String, *>, block: () -> T): T
	fun <T> runWithUniqueContext(vararg attributes: Pair<String, *>, block: () -> T): T
	fun delete(name: String)
	fun interpolate(expression: String): String

	fun getObjectMirror(name: String): ScriptObjectMirror?
	fun pushWorkingDirectory(path: Path)
	fun popWorkingDirectory()

	fun getPath(name: String): Path
}
