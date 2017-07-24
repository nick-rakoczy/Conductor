package urn.conductor

interface StandardComplexElementHandler<T : Any> : ComplexElementHandler<T> {
	fun process(element: T, engine: Engine, processChild: (Any) -> Unit)
}