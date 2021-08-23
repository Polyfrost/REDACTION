package net.wyvest.redaction.blackbar

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.wyvest.redaction.blackbar.text.CpsText
import net.wyvest.redaction.blackbar.text.TestText
import xyz.matthewtgm.requisite.util.MathHelper
import xyz.matthewtgm.requisite.util.RenderHelper
import java.awt.Color

object BlackBarManager {

    private var data: BlackBarData? = null

    private val color = Color(0, 0, 0, 85)
    var text = listOf(
        CpsText(),
        TestText()
    )

    private var firstTime = true

    fun initialize() {
        val resolution = ScaledResolution(Minecraft.getMinecraft())
        data = BlackBarData(resolution.scaledHeight - 22)
    }

    @SubscribeEvent
    fun render(event: RenderGameOverlayEvent.Pre) {
        data?.let {
            if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
                val scaledWidth = event.resolution.scaledWidth
                val scaledHeight = event.resolution.scaledHeight
                it.hiding = Minecraft.getMinecraft().currentScreen is GuiChat
                if (firstTime) {
                    firstTime = false
                    it.y = event.resolution.scaledHeight - 22
                    it.hiding = false
                }
                if (it.hiding) {
                    it.y = MathHelper.lerp(it.y.toFloat(), scaledHeight.toFloat() + 2, event.partialTicks / 4).toInt()
                } else if (!it.hiding && it.y != scaledHeight - 22) {
                    it.y = MathHelper.lerp(it.y.toFloat(), scaledHeight.toFloat() - 22, event.partialTicks / 4).toInt()
                }
                RenderHelper.drawRectEnhanced(0, it.y, scaledWidth, 22, color.rgb)
            }
        }
    }

}

private class BlackBarData(
    var y: Int
) {
    var hiding = false
}