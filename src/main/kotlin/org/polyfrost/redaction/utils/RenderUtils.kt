package org.polyfrost.redaction.utils

import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object RenderUtils {

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
        GlStateManager.color(colour.red / 255.0f, colour.green / 255.0f, colour.blue / 255.0f, colour.alpha / 255.0f)
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

    /**
     * Particle API (kotlinified by Wyvest)
     * This Api is free2use
     * But u have to mention me.
     *
     * @author Vitox
     * @version 3.0
     */
    fun connectPoints(xOne: Float, yOne: Float, xTwo: Float, yTwo: Float, color: Int, width: Float) {
        val alpha = (color shr 24 and 0xFF) / 255.0f
        val red = (color shr 16 and 0xFF) / 255.0f
        val green = (color shr 8 and 0xFF) / 255.0f
        val blue = (color and 0xFF) / 255.0f
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GlStateManager.color(red, green, blue, alpha)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glLineWidth(width)
        val tessellator = Tessellator.getInstance()
        val wr = tessellator.worldRenderer
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        wr.pos(xOne.toDouble(), yOne.toDouble(), 0.0).endVertex()
        wr.pos(xTwo.toDouble(), yTwo.toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
    }

    /**
     * Particle API (kotlinified by Wyvest)
     * This Api is free2use
     * But u have to mention me.
     *
     * @author Vitox
     * @version 3.0
     */
    fun drawCircle(x: Float, y: Float, radius: Float, color: Int) {
        val alpha = (color shr 24 and 0xFF) / 255.0f
        val red = (color shr 16 and 0xFF) / 255.0f
        val green = (color shr 8 and 0xFF) / 255.0f
        val blue = (color and 0xFF) / 255.0f
        GlStateManager.color(red, green, blue, alpha)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glLineWidth(1f)
        val tessellator = Tessellator.getInstance()
        val wr = tessellator.worldRenderer
        wr.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION)
        for (i in 0..360 step 60) {  // Akrz: Choose between divisors of 360 for better performance
            val angle = i * PI / 180.0
            wr.pos(x + sin(angle) * radius, y + cos(angle) * radius, 0.0).endVertex()
        }
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }

}