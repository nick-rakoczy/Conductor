package urn.conductor.aws.route53.recordsets

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.AaaaRecordSet


class AaaaRecordSetHandler : ElementHandler<AaaaRecordSet> {
	override val handles: Class<AaaaRecordSet>
		get() = AaaaRecordSet::class.java

	override fun process(element: AaaaRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}