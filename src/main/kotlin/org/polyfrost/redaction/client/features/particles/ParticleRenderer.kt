package org.polyfrost.redaction.client.features.particles

//? if >1.8.9
import com.mojang.blaze3d.vertex.VertexConsumer
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.redaction.client.RedactionConfig
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

object ParticleRenderer {
    private const val TWO_PI = PI * 2.0
    private const val SEGMENTS = 12
    private const val STEP = TWO_PI.toFloat() / SEGMENTS

    private val fastStorage = FastIntArrayList()
    private var grid: ParticleGrid? = null

    //? if >1.8.9 {
    fun drawParticles(
        vertexConsumer: VertexConsumer,
        //? if >=1.21.8 {
        pose: org.joml.Matrix3x2fc,
        //?} else
        //pose: com.mojang.blaze3d.vertex.PoseStack,
        particles: Collection<Particle>
    ) {
        val color = RedactionConfig.snowColor.argb or 0xFF000000.toInt()

        for (particle in particles) {
            val cx = particle.x
            val cy = particle.y

            var currentAngle = 0f
            var currentX = cx + sin(currentAngle) * particle.size
            var currentY = cy + cos(currentAngle) * particle.size

            repeat(SEGMENTS) {
                val nextAngle = currentAngle + STEP
                val nextX = cx + sin(nextAngle) * particle.size
                val nextY = cy + cos(nextAngle) * particle.size

                vertexConsumer
                    .addVertex(pose, cx, cy).setColor(color)
                    .addVertex(pose, currentX, currentY).setColor(color)
                    .addVertex(pose, nextX, nextY).setColor(color)

                currentAngle = nextAngle
                currentX = nextX
                currentY = nextY
            }
        }
    }

    fun connectParticles(
        vertexConsumer: VertexConsumer,
        //? if >=1.21.8 {
        pose: org.joml.Matrix3x2fc,
        //?} else
        //pose: com.mojang.blaze3d.vertex.PoseStack,
        particles: Collection<Particle>,
        mouseX: Int,
        mouseY: Int
    ) {
        val width = mc.window.guiScaledWidth
        val height = mc.window.guiScaledHeight

        if (grid == null || width != grid!!.width || height != grid!!.height) {
            grid = ParticleGrid(width, height, Particle.CONNECT_RANGE)
        }

        val grid = grid ?: return println("Grid is null")
        grid.rebuild(particles)

        val color = RedactionConfig.lineColor.argb or 0xFF000000.toInt()
        val halfWidth = RedactionConfig.lineWidth / 2f

        for ((i, particle) in particles.withIndex()) {
            if (!particle.isMouseOver(mouseX, mouseY)) continue

            val candidates = grid.query(particle.x.roundToInt(), particle.y.roundToInt(), fastStorage)
            for (j in candidates) {
                if (i >= j) {
                    continue
                }

                val other = particles.elementAt(j)
                if (abs(other.x - particle.x) >= Particle.CONNECT_RANGE || abs(other.y - particle.y) >= Particle.CONNECT_RANGE) {
                    continue
                }

                val dx = other.x - particle.x
                val dy = other.y - particle.y

                val distance = sqrt(dx * dx + dy * dy)
                if (distance >= Particle.CONNECT_RANGE || distance <= 0f) {
                    continue
                }

                val nx = dx / distance
                val ny = dy / distance
                val offsetX = ny * halfWidth
                val offsetY = -nx * halfWidth

                vertexConsumer
                    .addVertex(pose, particle.x + offsetX, particle.y + offsetY).setColor(color)
                    .addVertex(pose, other.x + offsetX, other.y + offsetY).setColor(color)
                    .addVertex(pose, other.x - offsetX, other.y - offsetY).setColor(color)
                    .addVertex(pose, particle.x - offsetX, particle.y - offsetY).setColor(color)
            }
        }
    }

    private fun VertexConsumer.addVertex(
        //? if >=1.21.8 {
        pose: org.joml.Matrix3x2fc,
        //?} else
        //pose: com.mojang.blaze3d.vertex.PoseStack,
        x: Float,
        y: Float
    ): VertexConsumer {
        //? if >=1.21.8 {
        return this.addVertexWith2DPose(
            //~ if <=1.21.10 'pose' -> 'pose as org.joml.Matrix3x2f'
            pose,
            x,
            y,
            //? if 1.21.8
            //0f
        )
        //?} else
        //return this.addVertex(pose.last(), x, y, 0f)
    }
    //?}

    //? if =1.8.9 {
    /*fun drawParticles(particles: Collection<Particle>) {
        val color = RedactionConfig.snowColor.argb or 0xFF000000.toInt()
        val r = (color shr 16 and 0xFF) / 255f
        val g = (color shr 8 and 0xFF) / 255f
        val b = (color and 0xFF) / 255f

        net.minecraft.client.render.platform.GlStateManager.enableBlend()
        net.minecraft.client.render.platform.GlStateManager.disableTexture()
        net.minecraft.client.render.platform.GlStateManager.blendFuncSeparate(770, 771, 1, 0)
        net.minecraft.client.render.platform.GlStateManager.color4f(r, g, b, 1f)

        val tess = net.minecraft.client.render.vertex.Tesselator.getInstance()
        val buf = tess.buffer

        buf.begin(4, net.minecraft.client.render.vertex.DefaultVertexFormat.POSITION)
        for (particle in particles) {
            val cx = particle.x
            val cy = particle.y

            var currentAngle = 0f
            var currentX = cx + sin(currentAngle) * particle.size
            var currentY = cy + cos(currentAngle) * particle.size

            repeat(SEGMENTS) {
                val nextAngle = currentAngle + STEP
                val nextX = cx + sin(nextAngle) * particle.size
                val nextY = cy + cos(nextAngle) * particle.size
                buf.vertex(cx.toDouble(), cy.toDouble(), 0.0).nextVertex()
                buf.vertex(currentX.toDouble(), currentY.toDouble(), 0.0).nextVertex()
                buf.vertex(nextX.toDouble(), nextY.toDouble(), 0.0).nextVertex()
                currentAngle = nextAngle
                currentX = nextX
                currentY = nextY
            }
        }
        tess.end()

        net.minecraft.client.render.platform.GlStateManager.enableTexture()
        net.minecraft.client.render.platform.GlStateManager.disableBlend()
        net.minecraft.client.render.platform.GlStateManager.color4f(1f, 1f, 1f, 1f)
    }

    fun connectParticles(particles: Collection<Particle>, mouseX: Int, mouseY: Int) {
        val width = mc.window.guiScaledWidth
        val height = mc.window.guiScaledHeight

        if (grid == null || width != grid!!.width || height != grid!!.height) {
            grid = ParticleGrid(width, height, Particle.CONNECT_RANGE)
        }

        val grid = grid ?: return println("Grid is null")
        grid.rebuild(particles)

        val color = RedactionConfig.lineColor.argb or 0xFF000000.toInt()
        val r = (color shr 16 and 0xFF) / 255f
        val g = (color shr 8 and 0xFF) / 255f
        val b = (color and 0xFF) / 255f
        val halfWidth = RedactionConfig.lineWidth / 2f

        net.minecraft.client.render.platform.GlStateManager.enableBlend()
        net.minecraft.client.render.platform.GlStateManager.disableTexture()
        net.minecraft.client.render.platform.GlStateManager.blendFuncSeparate(770, 771, 1, 0)
        net.minecraft.client.render.platform.GlStateManager.color4f(r, g, b, 1f)

        val tess = net.minecraft.client.render.vertex.Tesselator.getInstance()
        val buf = tess.buffer

        var hasConnections = false
        for ((i, particle) in particles.withIndex()) {
            val candidates = grid.query(particle.x.roundToInt(), particle.y.roundToInt(), fastStorage)
            for (j in candidates) {
                if (i >= j) continue

                val other = particles.elementAt(j)
                if (!particle.isMouseOver(mouseX, mouseY) && !other.isMouseOver(mouseX, mouseY)) continue
                if (abs(other.x - particle.x) >= Particle.CONNECT_RANGE || abs(other.y - particle.y) >= Particle.CONNECT_RANGE) continue

                val dx = other.x - particle.x
                val dy = other.y - particle.y

                val distance = sqrt(dx * dx + dy * dy)
                if (distance >= Particle.CONNECT_RANGE || distance <= 0f) continue

                if (!hasConnections) {
                    buf.begin(7, net.minecraft.client.render.vertex.DefaultVertexFormat.POSITION)
                    hasConnections = true
                }

                val nx = dx / distance
                val ny = dy / distance
                val offsetX = ny * halfWidth
                val offsetY = -nx * halfWidth

                buf.vertex((particle.x + offsetX).toDouble(), (particle.y + offsetY).toDouble(), 0.0).nextVertex()
                buf.vertex((other.x + offsetX).toDouble(), (other.y + offsetY).toDouble(), 0.0).nextVertex()
                buf.vertex((other.x - offsetX).toDouble(), (other.y - offsetY).toDouble(), 0.0).nextVertex()
                buf.vertex((particle.x - offsetX).toDouble(), (particle.y - offsetY).toDouble(), 0.0).nextVertex()
            }
        }

        if (hasConnections) tess.end()

        net.minecraft.client.render.platform.GlStateManager.enableTexture()
        net.minecraft.client.render.platform.GlStateManager.disableBlend()
        net.minecraft.client.render.platform.GlStateManager.color4f(1f, 1f, 1f, 1f)
    }
    *///?}
}
