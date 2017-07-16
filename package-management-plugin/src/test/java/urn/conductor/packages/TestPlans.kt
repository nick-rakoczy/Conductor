package urn.conductor.packages

import org.junit.Test
import urn.conductor.Main

class TestPlans {
	@Test fun nanoTest() = run("nano.xml")

	private fun file(name: String) = arrayOf("plugins=.", "file=./src/test/resources/$name")
	private fun run(name: String) = file(name).let(Main.Companion::main)
}