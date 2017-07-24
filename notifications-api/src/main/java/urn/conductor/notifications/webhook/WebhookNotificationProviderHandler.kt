package urn.conductor.notifications.webhook

import urn.conductor.Engine
import urn.conductor.StandardComplexElementHandler
import urn.conductor.notifications.xml.WebhookNotificationProvider

class WebhookNotificationProviderHandler : StandardComplexElementHandler<WebhookNotificationProvider> {
	override val handles: Class<WebhookNotificationProvider>
		get() = WebhookNotificationProvider::class.java

	override fun process(element: WebhookNotificationProvider, engine: Engine, processChild: (Any) -> Unit) {
		engine.put(element.id, WebhookNotificationProviderImpl(element, engine))
	}
}