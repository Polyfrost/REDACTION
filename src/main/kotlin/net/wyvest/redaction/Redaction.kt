package net.wyvest.redaction

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.dsl.openScreen
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.features.BlackBar
import net.wyvest.redaction.features.NameHighlight
import net.wyvest.redaction.features.ParticleManager
import net.wyvest.redaction.features.ServerManager
import net.wyvest.redaction.features.hitbox.Hitboxes
import net.wyvest.redaction.gui.HitboxPreviewGUI
import java.io.File

@Mod(
    name = Redaction.NAME,
    modid = Redaction.ID,
    version = Redaction.VERSION
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
    fun onFMLInitialization(event: FMLInitializationEvent) {
        CommandManager.INSTANCE.registerCommand(RedactionCommand.Companion::class.java)
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

    @Command(value = ID, description = "Access the REDACTION GUI.")
    class RedactionCommand {
        companion object {
            @Main
            fun main() {
                RedactionConfig.openGui()
            }

            @SubCommand(
                value = "hitbox",
                description = "Opens the hitbox config GUI for REDACTION."
            )
            class Sub {
                companion object {
                    @Main
                    fun hitbox() {
                        HitboxPreviewGUI().openScreen()
                    }
                }
            }
        }
    }
}