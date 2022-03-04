package uno.unicore.unicoreprovider.core.dto

import pro.gravit.launchserver.auth.core.HttpAuthCoreProvider

class AuthReport {
    lateinit var minecraftAccessToken: String
    lateinit var oauthAccessToken: String
    lateinit var oauthRefreshToken: String
    var oauthExpire: Long = System.currentTimeMillis()
    lateinit var  session: HttpAuthCoreProvider.HttpUserSession
}