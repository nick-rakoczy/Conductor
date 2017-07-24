package urn.conductor

import org.junit.Test
import urn.conductor.Main.Companion.main

class TestPlans {
	@Test fun containerHandler() = run("container-test.xml")
	@Test fun forHandler() = run("for-test.xml")
	@Test fun hostHandler() = run("host-test.xml")
	@Test fun includeHandler() = run("include-test/include-test.xml")
	@Test fun loadHandler() = run("load-test/load-test.xml")
	@Test fun neverFailHandler() = run("never-fail-test.xml")
	@Test fun scriptHandler() = run("script-test.xml")
	@Test fun vaultHandler() = run("vault-test/vault-test.xml")
	@Test fun ifUnlessHandler() = run("if-test.xml")
	@Test fun identityHandler() = run("identity-test.xml")
	@Test fun interpolation() = run("interpolation-test.xml")
	@Test fun ssh() = run("ssh-test/ssh-test.xml")
	@Test fun aggregateHandler() = run("aggregate-test.xml")
	@Test fun macroHandler() = run("macro-test.xml")
	@Test fun downloadHandler() = run("ssh-test/download-test.xml")
	@Test fun jsComponent() = run("urn/conductor/js/component/test.xml")

	private fun file(name: String) = arrayOf("plugins=.", "file=./src/test/resources/$name")
	private fun run(name: String) = file(name).let(::main)
}