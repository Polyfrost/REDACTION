package net.wyvest.redaction

import gg.essential.api.EssentialAPI
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.wyvest.redaction.command.RedactionCommand
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.features.BlackBar
import net.wyvest.redaction.features.NameHighlight
import net.wyvest.redaction.features.ParticleManager
import net.wyvest.redaction.features.ServerManager
import net.wyvest.redaction.features.hitbox.Hitboxes
import java.io.File

@Mod(
    name = Redaction.NAME,
    modid = Redaction.ID,
    version = Redaction.VERSION,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object Redaction {


    const val NAME = "REDACTION"
    const val VERSION = "1.3.5"
    const val ID = "redaction"
    val modDir = File(File("./W-OVERFLOW"), NAME)
    val isPatcher by lazy {
        try {
            Class.forName("club.sk1er.patcher.hooks.FontRendererHook")
            true
        } catch (ignored: Exception) {
            false
        }
    }
    var overrideHand = false
    var renderingHand = false

    @Mod.EventHandler
    private fun onFMLPreInitialization(event: FMLPreInitializationEvent) {
        if (!modDir.exists())
            modDir.mkdirs()
    }

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        RedactionConfig.preload()
        EssentialAPI.getCommandRegistry().registerCommand(RedactionCommand())
        EVENT_BUS.register(ParticleManager)
        EVENT_BUS.register(ServerManager)
        Hitboxes.initialize()
        ServerManager.initialize()
        NameHighlight.initialize()
    }

    @Mod.EventHandler
    fun onFMLPost(e: FMLLoadCompleteEvent) {
        BlackBar.initialize()
    }

}