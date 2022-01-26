package ru.zirconiamc.zirconiaprovider

import pro.gravit.launchserver.LaunchServer
import pro.gravit.launchserver.auth.core.HttpAuthCoreProvider

class ZirconiaProvider: HttpAuthCoreProvider() {
    var apiUrl: String? = null

    override fun init(server: LaunchServer) {
        if(apiUrl == null) {
            throw IllegalArgumentException("'apiUrl' can't be null");
        }

        getUserByUsernameUrl = apiUrl
        getUserByUUIDUrl = apiUrl
        authorizeUrl = apiUrl
        checkServerUrl = apiUrl
    }
}