package urn.conductor

interface ComponentRegistration {
	fun init() {

	}

	fun getAttributeHandlers(): List<AttributeHandler> = emptyList()
	fun getSimpleElementHandlers(): List<SimpleElementHandler> = emptyList()
	fun getComplexElementHandlers(): List<ElementHandler<*>> = emptyList()
	fun getPreloaders(): List<Preloader> = emptyList()
}