package urn.conductor

import org.apache.logging.log4j.LogManager

fun <T> attempt(block: () -> T): T? {
	try {
		return block()
	}
	catch (e: Exception) {
		if (LogManager.getLogger().isDebugEnabled) {
			LogManager.getLogger().debug("attempt failed", e.toString())
			e.printStackTrace()
		}
		return null
	}
}