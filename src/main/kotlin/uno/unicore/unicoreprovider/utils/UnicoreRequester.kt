package uno.unicore.unicoreprovider.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pro.gravit.launcher.Launcher
import java.io.IOException
import java.util.concurrent.TimeUnit

class UnicoreRequester(apiKey: String) {
    @Transient
    var client: OkHttpClient
    val JSON = "application/json; charset=utf-8".toMediaType()

    init {
        client = OkHttpClient
            .Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(UnicoreInterceptor(apiKey))
            .build();
    }

    @Throws(IOException::class)
    fun <T>post(url: String, data: T): HttpHelper {
        val body = Launcher.gsonManager.gson.toJson(data).toRequestBody(JSON)
        val request = Request.Builder().url(url).post(body).build()

        return client.newCall(request).execute().use { response ->
            HttpHelper(response)
        }
    }

    @Throws(IOException::class)
    fun get(url: String, param: String? = null, bearer: String? = null): HttpHelper {
        val urlTransform: String = if (param.isNullOrEmpty()) url else url.replace("%", param)
        val request = Request.Builder().url(urlTransform)
        if (!bearer.isNullOrEmpty())
            request.addHeader("Bearer", bearer)

        val requestBuild = request.build()

        return client.newCall(requestBuild).execute().use { response ->
            HttpHelper(response)
        }
    }
}