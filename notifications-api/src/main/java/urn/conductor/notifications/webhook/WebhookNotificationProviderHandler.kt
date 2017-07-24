package urn.conductor.notifications.webhook

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.notifications.xml.WebhookNotificationProvider

class WebhookNotificationProviderHandler : ComplexElementHandler<WebhookNotificationProvider> {
	override val handles: Class<WebhookNotificationProvider>
		get() = WebhookNotificationProvider::class.java

	override fun process(element: WebhookNotificationProvider, engine: Engine, processChild: (Any) -> Unit) {
		engine.put(element.id, WebhookNotificationProviderImpl(element, engine))
	}
}