package urn.conductor

import javax.xml.namespace.QName

interface CustomAttributeHandler<T> {
	fun getAttributes(element: T): Map<QName, String>
}