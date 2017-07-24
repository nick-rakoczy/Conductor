package urn.conductor.aws.route53.recordsets

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.SoaRecordSet

class SoaRecordSetHandler : ComplexElementHandler<SoaRecordSet> {
	override val handles: Class<SoaRecordSet>
		get() = SoaRecordSet::class.java

	override fun process(element: SoaRecordSet, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}