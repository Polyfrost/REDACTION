package xyz.qalcyo.redaction.render.colors

/**
 * Taken from Wynntils under GNU Affero General Public License v3.0
 * https://github.com/Wynntils/Wynntils/blob/development/LICENSE
 * @author Wynntils
 */
class CommonColors : CustomColor.SetBase {
    private constructor(rgb: Int) : super(rgb)
    private constructor() : super(-10f, -10f, -10f, 1f)

    companion object {
        val BLACK = CommonColors(0x000000)
        val RED = CommonColors(0xff0000)
        val GREEN = CommonColors(0x00ff00)
        val BLUE = CommonColors(0x0000ff)
        val YELLOW = CommonColors(0xffff00)
        val BROWN = CommonColors(0x563100)
        val PURPLE = CommonColors(0xb200ff)
        val CYAN = CommonColors(0x438e82)
        val LIGHT_GRAY = CommonColors(0xadadad)
        val GRAY = CommonColors(0x636363)
        val PINK = CommonColors(0xffb7b7)
        val LIGHT_GREEN = CommonColors(0x49ff59)
        val LIGHT_BLUE = CommonColors(0x00e9ff)
        val MAGENTA = CommonColors(0xff0083)
        val ORANGE = CommonColors(0xff9000)
        val WHITE = CommonColors(0xffffff)
        val RAINBOW = CommonColors()
        val CRITICAL = CommonColors()
        private val colors = arrayOf(
            BLACK, RED, GREEN, BLUE,
            YELLOW, BROWN, PURPLE, CYAN,
            LIGHT_GRAY, GRAY, PINK, LIGHT_GREEN,
            LIGHT_BLUE, MAGENTA, ORANGE, WHITE
        )
        private val names = arrayOf(
            "BLACK", "RED", "GREEN", "BLUE",
            "YELLOW", "BROWN", "PURPLE", "CYAN",
            "LIGHT_GRAY", "GRAY", "PINK", "LIGHT_GREEN",
            "LIGHT_BLUE", "MAGENTA", "ORANGE", "WHITE"
        )
        val set = ColorSet(colors, names)
    }
}