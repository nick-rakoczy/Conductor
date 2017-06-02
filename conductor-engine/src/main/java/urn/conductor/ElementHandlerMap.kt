package urn.conductor

class ElementHandlerMap {
	private val pairs = HashSet<Pair<Class<*>, ElementHandler<*>>>()

	val keys get() = pairs.map { it.first }
	val size get() = pairs.size

	operator fun <T : Any> get(type: Class<T>): ElementHandler<T>? = pairs.find { it.first == type }?.second as? ElementHandler<T>

	operator fun set(type: Class<*>, value: ElementHandler<*>) {
		pairs.removeIf {
			it.first == type
		}

		pairs.add(type to value)
	}
}