package urn.conductor.aws.route53.records

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.MxRecord

class MxRecordHandler : ComplexElementHandler<MxRecord> {
	override val handles: Class<MxRecord>
		get() = MxRecord::class.java

	override fun process(element: MxRecord, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}