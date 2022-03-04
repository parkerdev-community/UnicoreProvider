package uno.unicore.unicoreprovider.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

internal class UnicoreInterceptor(private val apiKey: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newRequest: Request = request.newBuilder()
            .addHeader("Authorization","Api-Key $apiKey")
            .build()
        return chain.proceed(newRequest)
    }
}