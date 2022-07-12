package net.wyvest.redaction.features

import cc.polyfrost.oneconfig.utils.Multithreading
import cc.polyfrost.oneconfig.utils.NetworkUtils
import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.wyvest.redaction.config.RedactionConfig

object ServerManager {

    private val serverList = hashMapOf<String, String>()

    fun initialize() {
        Multithreading.runAsync {
            val json =
                JsonParser().parse(NetworkUtils.getString("https://raw.githubusercontent.com/LunarClient/ServerMappings/master/servers.json")).asJsonArray
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