package urn.conductor.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import urn.conductor.ElementHandler
import urn.conductor.Engine
import urn.conductor.aws.xml.Credentials

class CredentialHandler : ElementHandler<Credentials> {
	override val handles: Class<Credentials>
		get() = Credentials::class.java

	override fun process(element: Credentials, engine: Engine, processChild: (Any) -> Unit) {
		val vaultRecordName = element.vaultRef?.let(engine::interpolate) ?: error("vault-ref undefined")
		val vaultEntry = engine.eval(vaultRecordName) as? Map<*, *> ?: error("Invalid vault reference")
		val accessKey = vaultEntry["username"] as? String ?: error("Invalid entry defined")
		val secretKey = vaultEntry["password"] as? String ?: error("Invalid entry defined")
		val credentials = BasicAWSCredentials(accessKey, secretKey)
		val credentialProvider = AWSStaticCredentialsProvider(credentials)
		val targetName = element.id
		engine.put(targetName, credentialProvider)
	}
}