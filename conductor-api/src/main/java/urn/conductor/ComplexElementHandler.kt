package urn.conductor

interface ComplexElementHandler<T : Any> {
	val handles: Class<T>
}