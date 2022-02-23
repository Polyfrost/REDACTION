package net.wyvest.redaction

import cc.woverflow.onecore.utils.Updater
import cc.woverflow.onecore.utils.command
import cc.woverflow.onecore.utils.openScreen
import gg.essential.api.utils.WebUtil
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.config.VigilanceConfig
import net.wyvest.redaction.features.BlackBar
import net.wyvest.redaction.features.ParticleManager
import net.wyvest.redaction.features.ServerManager
import net.wyvest.redaction.features.hitbox.Hitboxes
import net.wyvest.redaction.gui.HitboxPreviewGUI
import java.io.File

@Mod(
    name = Redaction.NAME,
    modid = Redaction.ID,
    version = Redaction.VERSION,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object Redaction {


    const val NAME = "REDACTION"
    const val VERSION = "1.1.0"
    const val ID = "redaction"
    val mc: Minecraft
        get() = Minecraft.getMinecraft()
    val modDir = File(File(mc.mcDataDir, "W-OVERFLOW"), NAME)
    var shouldApplyVigilanceMixin = false

    @Mod.EventHandler
    private fun onFMLPreInitialization(event: FMLPreInitializationEvent) {
        if (!modDir.exists())
            modDir.mkdirs()
        shouldApplyVigilanceMixin = WebUtil.fetchString("https://woverflow.cc/static/data/redaction_vigilance_mixin").toBoolean()
        Updater.addToUpdater(event.sourceFile, NAME, ID, VERSION, "W-OVERFLOW/REDACTION")
    }

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        RedactionConfig.initialize()
        command("redaction") {
            main {
                RedactionConfig.openScreen()
            }
            subCommand("hitbox", description = "Opens the hitbox config GUI for REDACTION.") {
                HitboxPreviewGUI().openScreen()
            }
        }

        command("vigilance", aliases = arrayListOf("vigilant")) {
            main {
                VigilanceConfig.openScreen()
            }
        }
        EVENT_BUS.register(ParticleManager)
        EVENT_BUS.register(ServerManager)
        Hitboxes.initialize()
        ServerManager.initialize()
    }

    @Mod.EventHandler
    fun onFMLPost(e: FMLLoadCompleteEvent) {
        BlackBar.initialize()
    }

}