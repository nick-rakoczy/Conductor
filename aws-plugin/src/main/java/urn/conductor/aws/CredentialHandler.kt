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
		val targetName = element.`as`
		when {
			element.vaultRef.isNotEmpty() -> {
				val vaultRef = element.vaultRef.orEmpty().let(engine::interpolate)
				val vaultEntry = engine.eval(vaultRef) as? Map<String, String> ?: error("Invalid vault defined")
				val accessKey = vaultEntry["username"] ?: error("Invalid entry defined")
				val secretKey = vaultEntry["password"] ?: error("Invalid entry defined")
				BasicAWSCredentials(accessKey, secretKey)
			}
			element.accessKey.isNotEmpty() && element.secretKey.isNotEmpty() -> {
				BasicAWSCredentials(element.accessKey, element.secretKey)
			}
			else -> error("Invalid credential tag, no accessKey+secretKey or vault-ref")
		}.let {
			AWSStaticCredentialsProvider(it)
		}.let {
			engine.put(targetName, it)
		}
	}
}