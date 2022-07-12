package net.wyvest.redaction.features

import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.libs.caffeine.cache.Cache
import cc.polyfrost.oneconfig.libs.caffeine.cache.Caffeine
import cc.polyfrost.oneconfig.libs.universal.ChatColor
import cc.polyfrost.oneconfig.utils.Multithreading
import club.sk1er.patcher.config.PatcherConfig
import club.sk1er.patcher.hooks.FontRendererHook
import club.sk1er.patcher.util.enhancement.text.EnhancedFontRenderer
import gg.essential.vigilance.gui.SettingsGui
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.config.RedactionConfig
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


object NameHighlight {

    private var counter: AtomicInteger = AtomicInteger(0)
    private var POOL: ThreadPoolExecutor = ThreadPoolExecutor(
        50, 50,
        0L, TimeUnit.SECONDS,
        LinkedBlockingQueue()
    ) { r ->
        Thread(
            r,
            "REDACTION Highlight Thread ${counter.incrementAndGet()}"
        )
    }
    private var check = false
    private var previous: OneColor? = null

    val cache: Cache<String, String> = Caffeine.newBuilder().executor(POOL).maximumSize(10000).build()

    fun initialize() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @JvmStatic
    fun highlightName(text: String): String {
        val playerName = Minecraft.getMinecraft().session.username
        if (text.contains(playerName)) {
            return if (text.contains("\u00A7")) {
                cache.getIfPresent(text) ?: if (RedactionConfig.asyncHighlight) run { Multithreading.runAsync { replaceAndPut(text) }; return@run text } else replaceAndPut(text)
            } else {
                text.replace(
                    playerName,
                    getColorCode() + playerName + ChatColor.RESET
                )
            }
        }
        return text
    }

    private fun replaceAndPut(string: String): String {
        val playerName = Minecraft.getMinecraft().session.username
        var number = -1
        var code: String? = null
        val array = string.split(playerName).toMutableList()
        for (split in array) {
            number += 1
            if (number % 2 == 0) {
                val subString = split.substringAfterLast("\u00A7", null.toString())
                code = if (subString != "null") {
                    subString.first().toString()
                } else {
                    null
                }
                continue
            } else {
                if (code != null) {
                    array[number] = "\u00A7$code" + array[number]
                }
            }
        }
        val joined =
            array.iterator().join(getColorCode() + playerName + ChatColor.RESET)
        cache.put(string, joined)
        return joined
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onGuiOpen(event: GuiOpenEvent) {
        if (!event.isCanceled) {
            if (event.gui is SettingsGui) {
                check = true
                previous = RedactionConfig.textColor
            } else if (event.gui == null && check) {
                if (RedactionConfig.textColor.rgb != (previous?.rgb ?: RedactionConfig.textColor.rgb)) {
                    if (Redaction.isPatcher && PatcherConfig.optimizedFontRenderer) {
                        for (enhancedFontRenderer in EnhancedFontRenderer.getInstances()) {
                            enhancedFontRenderer.invalidateAll()
                        }

                        FontRendererHook.forceRefresh = true
                    }
                }
                previous = null
                check = false
            }
        }
    }

    private fun Iterator<String>.join(separator: String): String {
        if (!this.hasNext()) return ""
        val first = this.next()
        if (!this.hasNext()) return first
        val buf = StringBuilder()
        buf.append(first)
        while (this.hasNext()) {
            buf.append(separator)
            val obj = this.next()
            buf.append(obj)
        }
        return buf.toString()
    }

    private fun getColorCode() = "${ChatColor.COLOR_CHAR}w" + (if (RedactionConfig.boldName) ChatColor.BOLD else "") + (if (RedactionConfig.italicsName) ChatColor.ITALIC else "")
}