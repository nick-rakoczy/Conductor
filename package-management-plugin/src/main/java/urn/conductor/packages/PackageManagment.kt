package urn.conductor.packages

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import urn.conductor.ssh.HostTransport

interface PackageManagment {
	fun install(transport: HostTransport, name: String, version: String)
	fun remove(transport: HostTransport, name: String)
	fun getInstalledVersion(transport: HostTransport, name: String): String?

	companion object {
		val implementations: Map<PackageManagementSystems, PackageManagment> by lazy {
			Reflections(SubTypesScanner(), TypeAnnotationsScanner()).let {
				it.getSubTypesOf(PackageManagment::class.java)
			}.map {
				it.getAnnotation(PackageManager::class.java)!!.value to it
			}.toMap().mapValues {
				it.value.newInstance()!!
			}
		}
	}
}