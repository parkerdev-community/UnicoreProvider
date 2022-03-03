package uno.unicore.unicoreprovider

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import pro.gravit.launcher.events.request.AuthRequestEvent
import pro.gravit.launcher.request.auth.AuthRequest
import pro.gravit.launchserver.LaunchServer
import pro.gravit.launchserver.auth.AuthException
import pro.gravit.launchserver.auth.core.AuthCoreProvider
import pro.gravit.launchserver.auth.core.HttpAuthCoreProvider.*
import pro.gravit.launchserver.auth.core.User
import pro.gravit.launchserver.auth.core.UserSession
import pro.gravit.launchserver.auth.core.interfaces.provider.AuthSupportExit
import pro.gravit.launchserver.manangers.AuthManager
import pro.gravit.launchserver.socket.response.auth.AuthResponse
import java.io.IOException
import java.util.*


class UnicoreProvider: AuthCoreProvider(), AuthSupportExit {
    private lateinit var apiUrl: String
    private lateinit var apiKey: String
    private lateinit var getUserByUsernameUrl: String
    private lateinit var getUserByUUIDUrl: String
    private lateinit var getUserByTokenUrl: String
    private lateinit var getAuthDetailsUrl: String
    private lateinit var refreshTokenUrl: String
    private lateinit var authorizeUrl: String
    private lateinit var deleteSessionUrl: String
    private lateinit var exitUserUrl: String

    @Transient
    private val logger: Logger = LogManager.getLogger()

    @Transient
    private lateinit var requester: UnicoreRequester

    override fun close() {}

    override fun getUserByUsername(username: String): User? {
        return try {
            requester.get(getUserByUsernameUrl, username).getOrThrow<HttpUser>()
        } catch (e: IOException) {
            logger.error(e)
            null
        }
    }

    override fun getUserByUUID(uuid: UUID): User? {
        return try {
            requester.get(getUserByUUIDUrl, uuid.toString()).getOrThrow<HttpUser>()
        } catch (e: IOException) {
            logger.error(e)
            null
        }
    }

    override fun getUserSessionByOAuthAccessToken(accessToken: String): UserSession? {
        return try {
            val result = requester.get(getUserByTokenUrl, bearer = accessToken)

            if (result.error != null) {
                if (result.error == AuthRequestEvent.OAUTH_TOKEN_EXPIRE)
                    throw OAuthAccessTokenExpired()

                return null
            }

            result.getOrThrow<HttpUserSession>()
        } catch (e: IOException) {
            logger.error(e)
            null
        }
    }

    override fun refreshAccessToken(refreshToken: String, context: AuthResponse.AuthContext): AuthManager.AuthReport? {
        return try {
            val response = requester.post(refreshTokenUrl, RefreshTokenRequest(refreshToken, context)).getOrThrow<AuthReport>()

            AuthManager.AuthReport(response.minecraftAccessToken, response.oauthAccessToken, response.oauthRefreshToken, response.oauthExpire!!, response.session)
        } catch (e: IOException) {
            logger.error(e)
            null
        }
    }

    override fun authorize(login: String, context: AuthResponse.AuthContext, password: AuthRequest.AuthPasswordInterface, minecraftAccess: Boolean): AuthManager.AuthReport {
        val result = requester.post(authorizeUrl, AuthorizeRequest(login, context, password, minecraftAccess))

        if (result.error != null)
            throw AuthException(result.error)

        var response = result.getOrThrow<AuthReport>()
        return AuthManager.AuthReport(response.minecraftAccessToken, response.oauthAccessToken, response.oauthRefreshToken, response.oauthExpire!!, response.session)
    }

    override fun deleteSession(session: UserSession): Boolean {
        requester.post(deleteSessionUrl, session)
        return true
    }

    override fun exitUser(user: User): Boolean {
        requester.post(exitUserUrl, user)
        return true
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun init(server: LaunchServer) {
        // Init provider config
        if(apiUrl == null)
            throw IllegalArgumentException("[UnicoreProvider] 'apiUrl' can't be null")

        if(apiKey == null)
            throw IllegalArgumentException("[UnicoreProvider] 'apiKey' can't be null")

        // Set-up urls
        getUserByUsernameUrl = "$apiUrl/auth/gravit/getUserByUsername/%"
        getUserByUUIDUrl = "$apiUrl/auth/gravit/getUserByUUID/%"
        getUserByTokenUrl = "$apiUrl/auth/gravit/getUserByToken"
        getAuthDetailsUrl = "$apiUrl/auth/gravit/getAuthDetails"
        refreshTokenUrl = "$apiUrl/auth/gravit/refreshToken"
        authorizeUrl = "$apiUrl/auth/gravit/authorize"
        deleteSessionUrl = "$apiUrl/auth/gravit/deleteSession"
        exitUserUrl = "$apiUrl/auth/gravit/exitUser"

        // Build UnicoreRequester
        requester = UnicoreRequester(apiKey)

        try {
            val kernel = requester.get("$apiUrl/auth/gravit/ping").getOrThrow<Kernel>()
            logger.info("[UnicoreProvider] Successfully initialized and send ping request [${kernel.uuid}]")
        } catch (e: IOException) {
            logger.error("[UnicoreProvider] apiKey not have permission 'kernel.unicore.provider' or incorrect!");
        }
    }

    class AuthReport {
        var minecraftAccessToken: String? = null
        var oauthAccessToken: String? = null
        var oauthRefreshToken: String? = null
        var oauthExpire: Long? = null
        var  session: HttpUserSession? = null
    }
}
