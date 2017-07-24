import com.google.gson.GsonBuilder
import org.apache.logging.log4j.LogManager
import org.junit.Test
import spark.Spark
import urn.conductor.Main
import java.util.UUID

class TestPlans {
	data class Notification(var text: String, var value: String)

	private val logger = LogManager.getLogger()

	@Test
	fun test() {
		val gson = GsonBuilder().create()
		val user = UUID.randomUUID().toString()
		val authToken = UUID.randomUUID().toString()
		val authHeader = com.google.common.net.HttpHeaders.AUTHORIZATION
		val message = UUID.randomUUID().toString()

		var callCount = 0
		Spark.post("/webhook") { req, res ->
			callCount++

			val header = req.headers(authHeader)
			assert(header == "Bearer $authToken")

			val body = req.body()
			val note = gson.fromJson(body, Notification::class.java)
			assert(note.text == "Hello, $user!")
			assert(note.value == message)

			logger.info("Received notification [$body] from user-agent ${req.userAgent()}")
		}

		val options = arrayOf(
				"plugins=.",
				"file=./src/test/resources/webhook-test.xml",
				"user=$user",
				"message=$message",
				"port=${Spark.port()}",
				"authHeader=$authHeader",
				"authToken=$authToken"
		)

		Main.Companion.main(options)

		assert(callCount == 1)

		Spark.stop()
	}
}