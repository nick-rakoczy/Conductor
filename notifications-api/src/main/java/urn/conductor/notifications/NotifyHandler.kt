package urn.conductor.notifications

import urn.conductor.ComplexElementHandler
import urn.conductor.Engine
import urn.conductor.notifications.xml.Notify

class NotifyHandler : ComplexElementHandler<Notify> {
	override val handles: Class<Notify>
		get() = Notify::class.java

	override fun process(element: Notify, engine: Engine, processChild: (Any) -> Unit) {
		val targetName = element.target.let(engine::interpolate)
		val message = element.value.let(engine::interpolate)
		val targetImpl = engine.get(targetName) as? NotificationProvider ?: error("Target name [$targetName] does not represent a NotificationProvider")
		targetImpl.notify(message)
	}

}