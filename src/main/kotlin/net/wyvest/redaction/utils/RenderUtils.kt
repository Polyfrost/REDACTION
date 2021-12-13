package net.wyvest.redaction.utils

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Particle API (kotlinified by Wyvest)
 * This Api is free2use
 * But u have to mention me.
 *
 * @author Vitox
 * @version 3.0
 */
object RenderUtils {
    fun connectPoints(xOne: Float, yOne: Float, xTwo: Float, yTwo: Float) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.8f)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glLineWidth(0.5f)
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
        for (i in 0..360) {
            wr.pos(x + sin(i * PI / 180.0) * radius, y + cos(i * PI / 180.0) * radius, 0.0)
                .endVertex()
        }
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }
}