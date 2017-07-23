package urn.conductor.aws.route53.recordsets

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.CnameRecordSet

class CnameRecordSetHandler : ElementHandler<CnameRecordSet> {
	override val handles: Class<CnameRecordSet>
		get() = CnameRecordSet::class.java

	override fun process(element: CnameRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}