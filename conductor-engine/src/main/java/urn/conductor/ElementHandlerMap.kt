package urn.conductor

class ElementHandlerMap {
	private val pairs = HashSet<Pair<Class<*>, ComplexElementHandler<*>>>()

	val keys get() = pairs.map { it.first }
	val size get() = pairs.size

	@Suppress("UNCHECKED_CAST")
	operator fun <T : Any> get(type: Class<T>): ComplexElementHandler<T>? = pairs.find { it.first == type }?.second as? ComplexElementHandler<T>

	operator fun set(type: Class<*>, value: ComplexElementHandler<*>) {
		pairs.removeIf {
			it.first == type
		}

		pairs.add(type to value)
	}
}