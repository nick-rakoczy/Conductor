package urn.conductor.notifications

interface NotificationProvider {
	fun notify(msg: String)

	companion object {
		val providerRegistrationName = "notificationProviders"
	}
}