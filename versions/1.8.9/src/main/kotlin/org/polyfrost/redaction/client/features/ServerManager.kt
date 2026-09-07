package org.polyfrost.redaction.client.features

import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import org.polyfrost.redaction.config.RedactionConfig
import kotlin.collections.iterator
import kotlin.text.get

object ServerManager {

    private val serverList = hashMapOf<String, String>()

    fun initialize() {
        Multithreading.runAsync {
            val json =
                JsonParser().parse(NetworkUtils.getString("https://servermappings.lunarclientcdn.com/servers.json")).asJsonArray
            for (element in json) {
                val serverJson = element.asJsonObject
                val addresses = serverJson["addresses"].asJsonArray
                for (address in addresses) {
                    serverList[address.asString] = serverJson["name"].asString
                }
            }
        }
    }

    fun getNameOfServer(ip: String?): String? {
        if (ip == null) return null
        for (server in serverList) {
            if (ip.endsWith(server.key, ignoreCase = true)) {
                return server.value
            }
        }
        return ip
    }

    @SubscribeEvent
    fun onServerJoined(event: ClientConnectedToServerEvent) {
        if (!event.isLocal) {
            RedactionConfig.lastServerIP = Minecraft.getMinecraft().currentServerData?.serverIP ?: ""
            RedactionConfig.save()
        }
    }
}