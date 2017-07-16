package urn.conductor

import org.junit.Test
import urn.conductor.Main.Companion.main

class TestPlans {
	@Test fun route53() = run("route53-test.xml")

	private fun file(name: String) = arrayOf("plugins=.", "file=./src/test/resources/$name")
	private fun run(name: String) = file(name).let(::main)
}