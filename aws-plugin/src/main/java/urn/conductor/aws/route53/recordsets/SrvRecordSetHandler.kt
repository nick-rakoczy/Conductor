package urn.conductor.aws.route53.recordsets

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.SrvRecordSet

class SrvRecordSetHandler : ElementHandler<SrvRecordSet> {
	override val handles: Class<SrvRecordSet>
		get() = SrvRecordSet::class.java

	override fun process(element: SrvRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}