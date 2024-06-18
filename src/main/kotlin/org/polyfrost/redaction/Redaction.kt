package org.polyfrost.redaction

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import org.polyfrost.redaction.command.RedactionCommand
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.features.BlackBar
import org.polyfrost.redaction.features.ParticleManager
import org.polyfrost.redaction.features.ServerManager

@Mod(
    name = Redaction.NAME,
    modid = Redaction.ID,
    version = Redaction.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object Redaction {

    const val NAME = "@NAME@"
    const val VERSION = "@VER@"
    const val ID = "@ID@"
    var overrideHand = false
    var renderingHand = false

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        RedactionConfig.preload()
        CommandManager.register(RedactionCommand())
        EVENT_BUS.register(ParticleManager)
        EVENT_BUS.register(ServerManager)
        ServerManager.initialize()
    }

    @Mod.EventHandler
    fun onFMLPost(e: FMLLoadCompleteEvent) {
        BlackBar.initialize()
    }

}