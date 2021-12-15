package net.wyvest.redaction.utils

import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.awt.Color


object GlUtil {
    /**
     * Adapted from XanderLib under GPL 3.0 license
     * https://github.com/isXander/XanderLib/blob/main/LICENSE
     *
     * @author isXander
     */
    fun drawRectangle(xPosition: Float, yPosition: Float, width: Float, height: Float, colour: Color) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        val worldrenderer = Tessellator.getInstance().worldRenderer
        GlStateManager.color(
            colour.red / 255.0f,
            colour.green / 255.0f,
            colour.blue / 255.0f,
            colour.alpha / 255.0f
        )
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(xPosition.toDouble(), (yPosition + height).toDouble(), 0.0).endVertex()
        worldrenderer.pos((xPosition + width).toDouble(), (yPosition + height).toDouble(), 0.0).endVertex()
        worldrenderer.pos((xPosition + width).toDouble(), yPosition.toDouble(), 0.0).endVertex()
        worldrenderer.pos(xPosition.toDouble(), yPosition.toDouble(), 0.0).endVertex()
        Tessellator.getInstance().draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.bindTexture(0)
        GlStateManager.color(1f, 1f, 1f, 1f)
    }

    fun drawRectEnhanced(x: Int, y: Int, width: Int, height: Int, color: Int) {
        Gui.drawRect(x, y, width + x, height + y, color)
    }

    fun drawGradientRect(xPosition: Float, yPosition: Float, width: Float, height: Float, startColor: Int, endColor: Int) {
        val startAlpha = (startColor shr 24 and 255).toFloat() / 255.0f
        val startRed = (startColor shr 16 and 255).toFloat() / 255.0f
        val startGreen = (startColor shr 8 and 255).toFloat() / 255.0f
        val startBlue = (startColor and 255).toFloat() / 255.0f
        val endAlpha = (endColor shr 24 and 255).toFloat() / 255.0f
        val endRed = (endColor shr 16 and 255).toFloat() / 255.0f
        val endGreen = (endColor shr 8 and 255).toFloat() / 255.0f
        val endBlue = (endColor and 255).toFloat() / 255.0f
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos((xPosition + width).toDouble(), yPosition.toDouble(), 0.0)
            .color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(xPosition.toDouble(), yPosition.toDouble(), 0.0)
            .color(startRed, startGreen, startBlue, startAlpha).endVertex()
        worldrenderer.pos(xPosition.toDouble(), (yPosition + height).toDouble(), 0.0)
            .color(endRed, endGreen, endBlue, endAlpha).endVertex()
        worldrenderer.pos((xPosition + width).toDouble(), (yPosition + height).toDouble(), 0.0)
            .color(endRed, endGreen, endBlue, endAlpha).endVertex()
        tessellator.draw()
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
    }
}