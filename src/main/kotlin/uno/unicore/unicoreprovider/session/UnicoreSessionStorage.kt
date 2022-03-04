package uno.unicore.unicoreprovider.session

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import pro.gravit.launchserver.LaunchServer
import pro.gravit.launchserver.auth.session.SessionStorage
import uno.unicore.unicoreprovider.utils.UnicoreRequester
import java.util.*
import java.util.stream.Stream

class UnicoreSessionStorage : SessionStorage() {
    var apiUrl: String? = null
    var apiKey: String? = null

    @Transient
    private val logger: Logger = LogManager.getLogger()
    @Transient
    private lateinit var requester: UnicoreRequester

    @Suppress("SENSELESS_COMPARISON")
    override fun init(server: LaunchServer) {
        if(apiUrl == null)
            throw IllegalArgumentException("UnicoreSessionStorage 'apiUrl' can't be null")

        if(apiKey == null)
            throw IllegalArgumentException("UnicoreSessionStorage 'apiKey' can't be null")

        super.init(server)

        requester = UnicoreRequester(apiKey!!)
        logger.info("UnicoreSessionStorage successfully initialized")
    }

    override fun getSessionData(session: UUID): ByteArray {
        TODO("Not yet implemented")
    }

    override fun getSessionsFromUserUUID(userUUID: UUID?): Stream<UUID> {
        TODO("Not yet implemented")
    }

    override fun writeSession(userUUID: UUID?, sessionUUID: UUID?, data: ByteArray?): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteSession(sessionUUID: UUID?): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun lockSession(sessionUUID: UUID?) {
        TODO("Not yet implemented")
    }

    override fun lockUser(userUUID: UUID?) {
        TODO("Not yet implemented")
    }

    override fun unlockSession(sessionUUID: UUID?) {
        TODO("Not yet implemented")
    }

    override fun unlockUser(userUUID: UUID?) {
        TODO("Not yet implemented")
    }
}