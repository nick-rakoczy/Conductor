package urn.conductor.aws.route53.recordsets

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.TxtRecordSet

class TxtRecordSetHandler : ComplexElementHandler<TxtRecordSet> {
	override val handles: Class<TxtRecordSet>
		get() = TxtRecordSet::class.java

	override fun process(element: TxtRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}