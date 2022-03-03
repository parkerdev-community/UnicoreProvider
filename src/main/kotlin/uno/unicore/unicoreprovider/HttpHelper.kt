package uno.unicore.unicoreprovider

import okhttp3.Response
import pro.gravit.launchserver.HttpRequester.SimpleError
import java.io.IOException
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class HttpHelper(val response: Response) {
    val body: String = response.body!!.string()
    var error: String? = null
    val gson: Gson = GsonBuilder().create()

    init {
        if (!response.isSuccessful)
            error = gson.fromJson(body, SimpleError::class.java)?.error
    }

    @Throws(IOException::class)
    inline fun <reified T>  getOrThrow(): T {
        if (!response.isSuccessful)
            throw IOException(response.toString())

        return gson.fromJson(body, T::class.java)
    }
}