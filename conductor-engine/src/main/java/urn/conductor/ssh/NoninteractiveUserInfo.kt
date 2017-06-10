package urn.conductor.ssh

import com.jcraft.jsch.UserInfo

object NoninteractiveUserInfo : UserInfo {
	override fun promptPassphrase(p0: String?): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getPassphrase(): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getPassword(): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun promptYesNo(p0: String?): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun showMessage(p0: String?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun promptPassword(p0: String?): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}