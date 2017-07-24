package urn.conductor.aws.route53.records

import urn.conductor.Engine
import urn.conductor.StandardComplexElementHandler
import urn.conductor.aws.xml.SrvRecord

class SrvRecordHandler : StandardComplexElementHandler<SrvRecord> {
	override val handles: Class<SrvRecord>
		get() = SrvRecord::class.java

	override fun process(element: SrvRecord, engine: Engine, processChild: (Any) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}
