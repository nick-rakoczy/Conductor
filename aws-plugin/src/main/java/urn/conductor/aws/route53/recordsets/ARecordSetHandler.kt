package urn.conductor.aws.route53.recordsets

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.ARecordSet

class ARecordSetHandler : ElementHandler<ARecordSet> {
	override val handles: Class<ARecordSet>
		get() = ARecordSet::class.java

	override fun process(element: ARecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}