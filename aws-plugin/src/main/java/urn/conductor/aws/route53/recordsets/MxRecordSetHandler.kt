package urn.conductor.aws.route53.recordsets

import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.MxRecordSet

class MxRecordSetHandler : ElementHandler<MxRecordSet> {
	override val handles: Class<MxRecordSet>
		get() = MxRecordSet::class.java

	override fun process(element: MxRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}