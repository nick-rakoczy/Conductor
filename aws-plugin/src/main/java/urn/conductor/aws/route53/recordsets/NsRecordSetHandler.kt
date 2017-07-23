package urn.conductor.aws.route53.recordsets

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.NsRecordSet

class NsRecordSetHandler : ElementHandler<NsRecordSet> {
	override val handles: Class<NsRecordSet>
		get() = NsRecordSet::class.java

	override fun process(element: NsRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}