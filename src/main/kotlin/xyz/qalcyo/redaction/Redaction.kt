package xyz.qalcyo.redaction

import com.google.gson.JsonParser
import gg.essential.universal.ChatColor
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import xyz.qalcyo.redaction.commands.RedactionCommand
import xyz.qalcyo.redaction.config.RedactionConfig
import xyz.qalcyo.redaction.hud.HudManager
import xyz.qalcyo.redaction.render.ScreenRenderer
import xyz.qalcyo.redaction.utils.Updater
import java.awt.Color
import java.io.File
import kotlin.reflect.typeOf

@Mod(
    name = Redaction.NAME,
    modid = Redaction.ID,
    version = Redaction.VERSION,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object Redaction {

    const val NAME = "REDACTION"
    const val VERSION = "0.2.0"
    const val ID = "redaction"
    val mc: Minecraft
        get() = Minecraft.getMinecraft()
    var await = false

    lateinit var jarFile: File
    val modDir = File(File(File(mc.mcDataDir, "config"), "Qalcyo"), NAME)
    val parser = JsonParser()
    var isMainMenu = false
    var isShadow = false

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
        Updater.update()
        EVENT_BUS.register(this)

        RedactionConfig.shadowColor = Color(RedactionConfig.color.rgb and 16579836 shr 2 or RedactionConfig.color.rgb and -16777216)
    }

    @Mod.EventHandler
    fun onFMLPost(e: FMLLoadCompleteEvent) {
        HudManager.initialize()
        await = false
    }

    @SubscribeEvent
    fun onasfsf(e: TickEvent.ClientTickEvent) {
        if (e.phase == TickEvent.Phase.START) {
            ScreenRenderer.refresh()
            if (await && mc.currentScreen != RedactionConfig.gui()) {
                await = false
                mc.refreshResources()
            }
        }
    }


    fun sendMessage(message: String) {
        if (mc.thePlayer == null)
            return
        val text =
            ChatComponentText(EnumChatFormatting.DARK_PURPLE.toString() + "[$NAME] " + ChatColor.RESET.toString() + " " + message)
        Minecraft.getMinecraft().thePlayer.addChatMessage(text)
    }

}