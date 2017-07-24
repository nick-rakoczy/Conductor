package urn.conductor.aws.route53.records

import urn.conductor.Engine
import urn.conductor.StandardComplexElementHandler
import urn.conductor.aws.xml.SoaRecord

class SoaRecordHandler : StandardComplexElementHandler<SoaRecord> {
	override val handles: Class<SoaRecord>
		get() = SoaRecord::class.java

	override fun process(element: SoaRecord, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}
