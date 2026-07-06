package org.polyfrost.redaction.client.features

import org.polyfrost.oneconfig.api.event.v1.eventHandler
import org.polyfrost.oneconfig.api.event.v1.events.ServerJoinEvent
import org.polyfrost.oneconfig.api.notifications.v1.Notifications
import org.polyfrost.oneconfig.utils.v1.JsonUtils
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.redaction.RedactionConstants
import org.polyfrost.redaction.client.RedactionConfig

object ServerManager {
    private var lastCacheAttempt = 0L
    private val serverList = hashMapOf<String, String>()

    fun initialize() {
        eventHandler<ServerJoinEvent> { _ ->
            saveLastServerIp()
        }

        if (RedactionConfig.serverPreview) {
            Multithreading.submit {
                cacheServerNames()
            }
        }
    }

    @JvmStatic
    fun getServerName(ip: String): String {
        if (serverList.isEmpty() && System.currentTimeMillis() - lastCacheAttempt > 10_000) {
            lastCacheAttempt = System.currentTimeMillis()

            // Block until the cache is populated so that we can return an actual value
            // This check is here in case the initial request either fails or if the user disabled the feature before startup but enabled it later
            cacheServerNames()
        }

        return serverList.entries.find { entry -> ip.endsWith(entry.key, ignoreCase = true) }?.value ?: ip
    }

    private fun saveLastServerIp() {
        if (!mc.isSingleplayer) { // Don't save the IP if we're connected/connecting to a singleplayer world
            RedactionConfig.lastServerIP = mc.currentServer?.ip ?: ""
            RedactionConfig.save()
        }
    }

    private fun cacheServerNames() {
        val json = JsonUtils.parseFromUrl("https://servermappings.lunarclientcdn.com/servers.json")
        if (json == null) {
            Notifications.info(RedactionConstants.NAME, "Failed to cache server names for direct connect preview: Could not fetch/parse")
            return
        }

        if (!json.isJsonArray) {
            Notifications.info(RedactionConstants.NAME, "Failed to cache server names for direct connect preview: Unexpected JSON format")
            return
        }

        for (element in json.asJsonArray) {
            if (!element.isJsonObject) {
                continue
            }

            val serverJson = element.asJsonObject
            val addressesElement = serverJson["addresses"] ?: continue
            if (!addressesElement.isJsonArray) {
                continue
            }

            for (address in addressesElement.asJsonArray) {
                if (!address.isJsonPrimitive) {
                    continue
                }

                val name = serverJson["name"] ?: continue
                if (!name.isJsonPrimitive) {
                    continue
                }

                serverList[address.asString] = name.asString
            }
        }
    }
}
