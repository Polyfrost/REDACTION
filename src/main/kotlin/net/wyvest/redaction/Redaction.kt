package net.wyvest.redaction

import cc.woverflow.wcore.utils.Updater
import cc.woverflow.wcore.utils.command
import cc.woverflow.wcore.utils.openGUI
import gg.essential.api.EssentialAPI
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.wyvest.redaction.config.RedactionConfig
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
    const val VERSION = "1.1.0-beta2"
    const val ID = "redaction"
    val mc: Minecraft
        get() = Minecraft.getMinecraft()
    val modDir = File(File(mc.mcDataDir, "W-OVERFLOW"), NAME)

    @Mod.EventHandler
    private fun onFMLPreInitialization(event: FMLPreInitializationEvent) {
        if (!modDir.exists())
            modDir.mkdirs()
        Updater.addToUpdater(event.sourceFile, NAME, ID, VERSION, "W-OVERFLOW/REDACTION")
    }

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        RedactionConfig.initialize()
        command("redaction") {
            main {
                RedactionConfig.openGUI()
            }
            subCommand("hitbox", description = "Opens the hitbox config GUI for REDACTION.") {
                EssentialAPI.getGuiUtil().openScreen(HitboxPreviewGUI())
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