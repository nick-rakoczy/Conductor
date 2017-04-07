package conductor

import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Configuration

@Configuration
class Conductor {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(Conductor::class.java, *args)
		}
	}
}