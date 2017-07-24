package urn.conductor.aws.route53.records

import urn.conductor.Engine
import urn.conductor.SimpleElementHandler
import urn.conductor.aws.xml.ObjectFactory
import javax.xml.namespace.QName

class AaaaRecordHandler : SimpleElementHandler {
	override val handles: QName
		get() = SimpleElementHandler.fromFactory(ObjectFactory::createAaaaRecord)

	override fun process(element: Any, engine: Engine) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}