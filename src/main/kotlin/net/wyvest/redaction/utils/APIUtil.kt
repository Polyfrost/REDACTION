package net.wyvest.redaction.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.wyvest.redaction.Redaction
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils
import java.net.URL
import java.util.*

/**
 * Stolen from Skytils under AGPLv3
 * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
 */
object APIUtil {

    private val builder: HttpClientBuilder =
        HttpClients.custom().setUserAgent("${Redaction.NAME}/${Redaction.VERSION}")
            .addInterceptorFirst { request: HttpRequest, _: HttpContext? ->
                if (!request.containsHeader("Pragma")) request.addHeader("Pragma", "no-cache")
                if (!request.containsHeader("Cache-Control")) request.addHeader("Cache-Control", "no-cache")
            }

    fun getJSONResponse(urlString: String): JsonObject {
        val client = builder.build()
        try {
            val request = HttpGet(URL(urlString).toURI())
            request.protocolVersion = HttpVersion.HTTP_1_1
            val response: HttpResponse = client.execute(request)
            val entity = response.entity
            if (response.statusLine.statusCode == 200) {
                return JsonParser.parseString(EntityUtils.toString(entity)).asJsonObject
            } else {
                if (urlString.startsWithAny(
                        "https://api.ashcon.app/mojang/v2/user/",
                        "https://api.hypixel.net/"
                    )
                ) {
                    val errorStream = entity.content
                    Scanner(errorStream).use { scanner ->
                        scanner.useDelimiter("\\Z")
                        val error = scanner.next()
                        if (error.startsWith("{")) {
                            return JsonParser.parseString(error).asJsonObject
                        }
                    }
                }
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
            Redaction.sendMessage("§cAn error has occured whilst fetching a resource. See logs for more details.")
        } finally {
            client.close()
        }
        return JsonObject()
    }
}