package net.wyvest.redaction

import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.wyvest.redaction.blackbar.BlackBarManager
import net.wyvest.redaction.commands.RedactionCommand
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.utils.Updater
import xyz.matthewtgm.requisite.util.ChatHelper
import xyz.matthewtgm.requisite.util.ForgeHelper
import java.io.File

@Mod(name = Redaction.NAME, modid = Redaction.ID, version = Redaction.VERSION, modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter")
object Redaction {

    const val NAME = "REDACTION"
    const val VERSION = "0.1.0-BETA1"
    const val ID = "redaction"
    val mc: Minecraft
        get() = Minecraft.getMinecraft()

    lateinit var jarFile: File
    private val modDir = File(File(File(mc.mcDataDir, "config"), "Wyvest"), NAME)

    @Mod.EventHandler
    private fun onFMLPreInitialization(event: FMLPreInitializationEvent) {
        if (!modDir.exists())
            modDir.mkdirs()
        jarFile = event.sourceFile
    }

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        RedactionConfig.initialize()
        RedactionCommand.register()

        BlackBarManager.initialize()
        ForgeHelper.registerEventListener(BlackBarManager)
        Updater.update()
    }

    fun sendMessage(message: String?) {
        ChatHelper.sendMessage(EnumChatFormatting.DARK_PURPLE.toString() + "[$NAME] ", message)
    }

}