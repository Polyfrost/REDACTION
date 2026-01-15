package org.polyfrost.redaction.client.features.particles

import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.OmniResolution
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.locationOrThrow
import org.polyfrost.redaction.RedactionConstants
import org.polyfrost.redaction.client.RedactionConfig
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

private val PARTICLE_PIPELINE = OmniRenderPipelines.builderWithDefaultShader(
    location = locationOrThrow(RedactionConstants.ID, "particles"),
    vertexFormat = DefaultVertexFormats.POSITION_COLOR,
    drawMode = DrawMode.TRIANGLES,
).setBlendState(OmniBlendState.ALPHA).build()

private val fastStorage = FastIntArrayList()
private var grid: ParticleGrid? = null

fun connectParticles(
    context: OmniRenderingContext,
    particles: Collection<Particle>,
    mouseX: Int,
    mouseY: Int
) {
    val width = OmniResolution.scaledWidth
    val height = OmniResolution.scaledHeight

    if (grid == null || width != grid!!.width || height != grid!!.height) {
        grid = ParticleGrid(width, height, Particle.CONNECT_RANGE)
    }

    val grid = grid ?: return println("Grid is null")
    grid.rebuild(particles)

    val pipeline = OmniRenderPipelines.POSITION_COLOR
    val buffer = pipeline.createBufferBuilder()
    val color = RedactionConfig.lineColor.rgba.ensureFullAlpha()
    val halfWidth = RedactionConfig.lineWidth / 2f
    val range = Particle.CONNECT_RANGE

    for ((i, particle) in particles.withIndex()) {
        if (!particle.isMouseOver(mouseX, mouseY)) continue

        val candidates = grid.query(particle.x.roundToInt(), particle.y.roundToInt(), fastStorage)
        for (j in candidates) {
            if (i >= j) {
                continue
            }

            val other = particles.elementAt(j)
            if (abs(other.x - particle.x) >= range || abs(other.y - particle.y) >= range) {
                continue
            }

            val dx = other.x - particle.x
            val dy = other.y - particle.y

            val distance = sqrt(dx * dx + dy * dy)
            if (distance >= range || distance <= 0f) {
                continue
            }

            val nx = dx / distance
            val ny = dy / distance
            val offsetX = ny * halfWidth
            val offsetY = -nx * halfWidth

            buffer
                .vertex(context.pose, (particle.x + offsetX).toDouble(), (particle.y + offsetY).toDouble(), 0.0)
                .color(color)
                .next()
            buffer
                .vertex(context.pose, (other.x + offsetX).toDouble(), (other.y + offsetY).toDouble(), 0.0)
                .color(color)
                .next()
            buffer
                .vertex(context.pose, (other.x - offsetX).toDouble(), (other.y - offsetY).toDouble(), 0.0)
                .color(color)
                .next()
            buffer
                .vertex(context.pose, (particle.x - offsetX).toDouble(), (particle.y - offsetY).toDouble(), 0.0)
                .color(color)
                .next()
        }
    }

    buffer.buildOrNull()?.drawAndClose(pipeline)
}

fun drawParticles(context: OmniRenderingContext, particles: Collection<Particle>) {
    val color = RedactionConfig.snowColor.rgba.ensureFullAlpha()
    val twoPi = PI * 2.0
    val segments = 12
    val step = twoPi / segments

    val buffer = OmniBufferBuilders.create(DrawMode.TRIANGLES, DefaultVertexFormats.POSITION_COLOR)
    for (particle in particles) {
        val cx = particle.x.toDouble()
        val cy = particle.y.toDouble()

        var currentAngle = 0.0
        var currentX = cx + sin(currentAngle) * particle.size
        var currentY = cy + cos(currentAngle) * particle.size
        repeat(segments) {
            val nextAngle = currentAngle + step
            val nextX = cx + sin(nextAngle) * particle.size
            val nextY = cy + cos(nextAngle) * particle.size

            buffer.vertex(context.pose, cx, cy, 0.0).color(color).next()
            buffer.vertex(context.pose, currentX, currentY, 0.0).color(color).next()
            buffer.vertex(context.pose, nextX, nextY, 0.0).color(color).next()

            currentAngle = nextAngle
            currentX = nextX
            currentY = nextY
        }
    }

    buffer.buildOrNull()?.drawAndClose(PARTICLE_PIPELINE)
}

private fun Int.ensureFullAlpha(): Int {
    return if (this and 0xFF000000.toInt() == 0) {
        this or 0xFF000000.toInt()
    } else this
}
