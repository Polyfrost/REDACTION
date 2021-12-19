package net.wyvest.redaction.features

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.wyvest.redaction.Redaction.mc
import net.wyvest.redaction.config.RedactionConfig

object ServerManager {

    @SubscribeEvent
    fun onServerJoined(event: ClientConnectedToServerEvent) {
        if (!event.isLocal) {
            RedactionConfig.lastServerIP = mc.currentServerData?.serverIP ?: ""
            RedactionConfig.markDirty()
            RedactionConfig.writeData()
        }
    }
}