package ru.unicorecms.unicoreprovider.utils

import okhttp3.Response
import pro.gravit.launcher.Launcher
import pro.gravit.launchserver.HttpRequester.SimpleError
import java.io.IOException

class HttpHelper(val response: Response) {
    val body: String = response.body!!.string()
    var error: String? = null

    init {
        if (!response.isSuccessful)
            error = Launcher.gsonManager.gson.fromJson(body, SimpleError::class.java)?.error
    }

    @Throws(IOException::class)
    inline fun <reified T>  getOrThrow(): T {
        if (!response.isSuccessful)
            throw IOException(response.toString())

        return Launcher.gsonManager.gson.fromJson(body, T::class.java)
    }
}