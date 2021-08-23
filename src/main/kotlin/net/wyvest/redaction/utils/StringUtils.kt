package net.wyvest.redaction.utils

import net.minecraft.util.EnumChatFormatting
import org.apache.commons.lang3.StringUtils as ApacheStringUtils

/**
 * Adapted from Skytils under AGPLv3
 * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
 */
fun String?.startsWithAny(vararg sequences: CharSequence?) = ApacheStringUtils.startsWithAny(this, *sequences)

/**
 * Adapted from Skytils under AGPLv3
 * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
 */
fun String?.equalsAny(vararg sequences: CharSequence?): Boolean {
    if (this == null) return false
    return sequences.any { it != null && this.equals(it as String, true) }
}

/**
 * Adapted from Skytils under AGPLv3
 * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
 */
fun String?.containsAny(vararg sequences: CharSequence?): Boolean {
    if (this == null) return false
    return sequences.any { it != null && this.contains(it, true) }
}

fun String.withoutFormattingCodes(): String = EnumChatFormatting.getTextWithoutFormattingCodes(this)