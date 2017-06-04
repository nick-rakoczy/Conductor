package urn.conductor.aws

import com.amazonaws.ClientConfiguration
import com.amazonaws.PredefinedClientConfigurations
import urn.conductor.AttributeHandler
import urn.conductor.Engine
import java.beans.Introspector
import javax.xml.namespace.QName

class ClientConfigurationRefHandler : AttributeHandler {
	override val handles: QName
		get() = QName("https://amazonaws.com", "client-configuration-ref")
	override val priority: Int
		get() = 100

	override fun process(element: Any, attribute: String, engine: Engine, proceed: () -> Unit) {
		val map = engine.getObjectMirror(attribute.let(engine::interpolate)) ?: error("invalid client-configuration-ref")
		val base = map["base"] ?: "default"

		val config = when (base) {
			"default" -> PredefinedClientConfigurations.defaultConfig()
			"dynamo" -> PredefinedClientConfigurations.dynamoDefault()
			"swf" -> PredefinedClientConfigurations.swfDefault()
			else -> error("invalid base")
		}.let {
			ClientConfiguration(it)
		}

		Introspector.getBeanInfo(ClientConfiguration::class.java).propertyDescriptors.filter {
			it.writeMethod != null
		}.map {
			it to map[it.name]
		}.filter { (prop, _) ->
			prop.writeMethod != null
		}.forEach { (prop, value) ->
			prop.writeMethod.invoke(config, value)
		}

		engine.put(clientConfigurationPropertyName, config)
		proceed()
		engine.delete(clientConfigurationPropertyName)
	}

	companion object {
		val clientConfigurationPropertyName = "aws:client-configuration"
	}
}