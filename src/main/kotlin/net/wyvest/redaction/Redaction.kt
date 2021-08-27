package net.wyvest.redaction

import com.google.gson.JsonParser
import gg.essential.universal.ChatColor
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.wyvest.redaction.commands.RedactionCommand
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.hud.HudManager
import net.wyvest.redaction.utils.Updater
import java.io.File

@Mod(name = Redaction.NAME, modid = Redaction.ID, version = Redaction.VERSION, modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter")
object Redaction {

    const val NAME = "REDACTION"
    const val VERSION = "0.1.1"
    const val ID = "redaction"
    val mc: Minecraft
        get() = Minecraft.getMinecraft()

    lateinit var jarFile: File
    val modDir = File(File(File(mc.mcDataDir, "config"), "Wyvest"), NAME)
    val parser = JsonParser()

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

        HudManager.initialize()
        Updater.update()
    }

    fun sendMessage(message: String) {
        if (mc.thePlayer == null)
            return
        val text = ChatComponentText(EnumChatFormatting.DARK_PURPLE.toString() + "[$NAME] " + ChatColor.RESET.toString() + " " + message)
        Minecraft.getMinecraft().thePlayer.addChatMessage(text)
    }

}