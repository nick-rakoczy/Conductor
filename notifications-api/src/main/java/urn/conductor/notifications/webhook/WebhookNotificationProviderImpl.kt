package urn.conductor.notifications.webhook

import okhttp3.CacheControl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.logging.log4j.LogManager
import urn.conductor.Engine
import urn.conductor.notifications.NotificationProvider
import urn.conductor.notifications.xml.WebhookNotificationProvider

class WebhookNotificationProviderImpl(private val element: WebhookNotificationProvider, private val engine: Engine) : NotificationProvider {
	private val logger = LogManager.getLogger()
	private val client = OkHttpClient()
	
	override fun notify(msg: String) {
		val builder = Request.Builder()
		element.header.forEach {
			builder.addHeader(it.name.let(engine::interpolate), it.value.let(engine::interpolate))
		}
		builder.cacheControl(CacheControl.FORCE_NETWORK)

		val url = element.href.let(engine::interpolate)
		builder.url(url)

		val mimeTypeString = element.mimeType.let(engine::interpolate)
		val mimeType = mimeTypeString.let(MediaType::parse) ?: error("Invalid mime type [$mimeTypeString]")

		val body = engine.runWithUniqueContext(element.template.messageRef to msg) {
			element.template.value.let(engine::interpolate)
		}
		val requestBody = RequestBody.create(mimeType, body)!!
		builder.post(requestBody)

		val request = builder.build()
		val call = client.newCall(request)
		call.execute()

		logger.info("Sent notification to [$url] [$body]")
	}
}