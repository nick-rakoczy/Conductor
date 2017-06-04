package urn.conductor

import java.beans.Introspector
import javax.xml.namespace.QName

object AutoAttributeHandler : CustomAttributeHandler<Any> {
	override fun getAttributes(element: Any): Map<QName, String> {
		val beanInfo = Introspector.getBeanInfo(element.javaClass)
		val properties = beanInfo.propertyDescriptors
		val otherAttributesProperties = properties.filter {
			it.name == "otherAttributes"
		}.singleOrNull() ?: return emptyMap()

		val result = otherAttributesProperties.readMethod.invoke(element)
		val mapResult = result as? Map<*, *> ?: return emptyMap()

		@Suppress("UNCHECKED_CAST")
		return mapResult as? Map<QName, String> ?: emptyMap()
	}
}