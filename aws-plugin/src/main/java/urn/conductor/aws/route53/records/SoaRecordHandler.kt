package urn.conductor.aws.route53.records

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.SoaRecord

class SoaRecordHandler : ComplexElementHandler<SoaRecord> {
	override val handles: Class<SoaRecord>
		get() = SoaRecord::class.java

	override fun process(element: SoaRecord, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}
