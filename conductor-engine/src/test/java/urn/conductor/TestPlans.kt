package urn.conductor

import org.junit.Test
import urn.conductor.Main.Companion.main

class TestPlans {
	@Test
	fun test() {
		val testFiles = HashSet<String>()

		//testFiles.add("context-test.xml")

		testFiles.map(this::file).forEach(::main)
	}

	private fun file(name: String) = arrayOf("plugins=.", "file=./src/test/resources/$name")
}