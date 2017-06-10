package urn.conductor

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.Session
import org.apache.logging.log4j.LogManager
import java.lang.management.ThreadInfo
import java.nio.file.Path
import javax.script.ScriptEngine
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSchema

operator fun ScriptEngine.set(name: String, value: Any) {
	this.put(name, value)
}

fun <T : Any> T.coerceToNull(vararg value: T): T? {
	if (this in value) {
		return null
	} else {
		return this
	}
}

fun <T> attempt(block: () -> T): T? {
	try {
		return block()
	}
	catch (e: Exception) {
		LogManager.getLogger().debug("attempt failed", e.toString())
		return null
	}
}

fun attemptUnit(block: () -> Unit): Boolean = attempt {
	block()
	true
} ?: false

fun <T : Any, R : Any> Iterable<T>.attemptMap(block: (T) -> R): Iterable<R> = this.map {
	attempt {
		block(it)
	}
}.filterNotNull()


inline val <reified T : Any> T.singletonArray: Array<T>
	get() = arrayOf(this)

val Class<*>.xmlNamespace: String?
	get() = this.getAnnotation(XmlRootElement::class.java)?.namespace?.coerceToNull("##default")
			?: this.`package`?.getAnnotation(XmlSchema::class.java)?.namespace

val Class<*>.xmlElementName: String?
	get() = this.getAnnotation(XmlRootElement::class.java)?.name

val Path.absolutePathString get() = this.toAbsolutePath().normalize().toString()
