package net.wyvest.redaction.features

import gg.essential.api.utils.Multithreading
import gg.essential.lib.caffeine.cache.Cache
import gg.essential.lib.caffeine.cache.Caffeine
import gg.essential.universal.ChatColor
import net.minecraft.client.Minecraft
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.utils.invalidateableLazy
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

object NameHighlight {
    internal val colorDelegate = invalidateableLazy {
        return@invalidateableLazy when (RedactionConfig.textColor) {
            0 -> ChatColor.BLACK.toString()
            1 -> ChatColor.DARK_BLUE.toString()
            2 -> ChatColor.DARK_GREEN.toString()
            3 -> ChatColor.DARK_AQUA.toString()
            4 -> ChatColor.DARK_RED.toString()
            5 -> ChatColor.DARK_PURPLE.toString()
            6 -> ChatColor.GOLD.toString()
            7 -> ChatColor.GRAY.toString()
            8 -> ChatColor.DARK_GRAY.toString()
            9 -> ChatColor.BLUE.toString()
            10 -> ChatColor.GREEN.toString()
            11 -> ChatColor.AQUA.toString()
            12 -> ChatColor.RED.toString()
            13 -> ChatColor.LIGHT_PURPLE.toString()
            14 -> ChatColor.YELLOW.toString()
            else -> ChatColor.WHITE.toString()
        }
    }

    private val highlightColor by colorDelegate

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

    val cache: Cache<String, String> = Caffeine.newBuilder().executor(POOL).maximumSize(10000).build()

    @JvmStatic
    fun highlightName(text: String): String {
        val playerName = Minecraft.getMinecraft().session.username
        if (text.contains(playerName)) {
            return if (text.contains("\u00A7")) {
                cache.getIfPresent(text) ?: if (RedactionConfig.asyncHighlight) run { Multithreading.runAsync { replaceAndPut(text) }; return@run text } else replaceAndPut(text)
            } else {
                text.replace(
                    playerName,
                    highlightColor + playerName + ChatColor.RESET
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
            array.iterator().join(highlightColor + playerName + ChatColor.RESET)
        cache.put(string, joined)
        return joined
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
}