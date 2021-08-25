package net.wyvest.redaction.utils

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
}