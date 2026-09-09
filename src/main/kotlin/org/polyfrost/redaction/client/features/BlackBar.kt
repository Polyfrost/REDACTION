package org.polyfrost.redaction.client.features

//? if >1.8.9 {
import net.minecraft.client.AttackIndicatorStatus
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import org.polyfrost.redaction.client.RedactionConfig
import org.polyfrost.redaction.mixin.client.accessor.HudAccessor
import org.polyfrost.redaction.mixin.client.accessor.SpectatorGuiAccessor
import net.minecraft.client.gui.spectator.categories.SpectatorPage
//?} else {
/*
import net.minecraft.client.gui.GuiElement
import net.minecraft.client.gui.screen.game.ChatScreen
import net.minecraft.client.render.platform.GlStateManager
import net.minecraft.client.render.vertex.DefaultVertexFormat
import net.minecraft.client.render.vertex.Tesselator
import net.minecraft.entity.living.player.PlayerEntity
import net.minecraft.resource.Identifier
import org.polyfrost.oneconfig.utils.v1.Multithreading
import org.polyfrost.redaction.client.RedactionConfig
import org.polyfrost.redaction.mixin.client.legacy.accessor.MinecraftAccessor
import java.awt.Color
import java.awt.event.ActionListener
import javax.swing.Timer
*///?}
import org.polyfrost.oneconfig.utils.v1.dsl.mc

import kotlin.math.roundToInt

object BlackBar {
    //? if >1.8.9 {
    private val HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background")
    private val HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress")

    private const val HOTBAR_WIDTH = 182
    private const val HALF_HOTBAR_WIDTH = HOTBAR_WIDTH / 2
    private const val HOTBAR_HEIGHT = 22

    private var currentX = -1f
    private var currentY = -1f

    @JvmStatic
    fun renderBlackBar(graphics: GuiGraphicsExtractor, hud: HudAccessor, deltaTracker: DeltaTracker, player: Player) {
        val screenCenter = graphics.guiWidth() / 2

        //~ if <=1.21.4 'selectedSlot' -> 'selected'
        val targetX = screenCenter - HALF_HOTBAR_WIDTH + player.inventory.selectedSlot * 20f

        val fixedY = graphics.guiHeight() - HOTBAR_HEIGHT
        //~ if <26.2 'gui.screen()' -> 'screen'
        val targetY = if (mc.gui.screen() is ChatScreen) graphics.guiHeight() + 2f else fixedY.toFloat()

        if (currentX == -1f) currentX = targetX
        if (currentY == -1f) currentY = targetY

        val partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true)
        currentX = lerp(currentX, targetX, partialTicks / 4f)
        currentY = lerp(currentY, targetY, partialTicks / 4f)

        val yInt = currentY.roundToInt()

        if (currentY < graphics.guiHeight()) {
            drawBackground(graphics, currentX.roundToInt(), yInt)
        }

        drawSlots(graphics, hud, player, deltaTracker, screenCenter, fixedY + 3)

        if (mc.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
            drawAttackIndicator(graphics, player, screenCenter, fixedY)
        }
    }

    @JvmStatic
    fun extractSpectatorBlackBar(
        graphics: GuiGraphicsExtractor,
        alpha: Float,
        screenCenter: Int,
        y: Int,
        page: SpectatorPage,
        spectatorGui: SpectatorGuiAccessor
    ) {
        val xInt = screenCenter - HALF_HOTBAR_WIDTH + page.selectedSlot * 20
        drawBackground(graphics, xInt, y, alpha, page.selectedSlot >= 0)

        for (slot in 0..8) {
            val slotX = screenCenter - HALF_HOTBAR_WIDTH + slot * 20 + 3
            val item = page.getItem(slot)
            
            //~ if <26.1 'invokeExtractSlot' -> 'invokeRenderSlot'
            spectatorGui.invokeExtractSlot(
                graphics,
                slot,
                slotX,
                y + 3f,
                alpha,
                item
            )
        }
    }

    private fun drawBackground(graphics: GuiGraphicsExtractor, xInt: Int, yInt: Int, alpha: Float = 1.0f, drawSelection: Boolean = true) {
        graphics.fill(
            0,
            yInt,
            graphics.guiWidth(),
            yInt + HOTBAR_HEIGHT,
            applyAlpha(RedactionConfig.blackbarColor.argb, alpha)
        )

        if (drawSelection) {
            graphics.fill(
                xInt,
                yInt,
                xInt + 22,
                yInt + 22,
                applyAlpha(RedactionConfig.blackbarItemColor.argb, alpha)
            )
        }
    }

    private fun drawSlots(
        graphics: GuiGraphicsExtractor,
        hud: HudAccessor,
        player: Player,
        deltaTracker: DeltaTracker,
        screenCenter: Int,
        slotY: Int
    ) {
        var seed = 1

        //~ if <26.1 'invokeExtractSlot' -> 'invokeRenderSlot' {
        for (i in 0..8) {
            val slotX = screenCenter - HALF_HOTBAR_WIDTH + i * 20 + 3

            hud.invokeExtractSlot(
                graphics,
                slotX,
                slotY,
                deltaTracker,
                player,
                player.inventory.getItem(i),
                seed++
            )

            if (RedactionConfig.blackbarSlotNumbers) {
                //~ if <26.1 'text' -> 'drawString'
                graphics.text(
                    mc.font,
                    (i + 1).toString(),
                    slotX,
                    slotY,
                    0xFFFFFFFF.toInt(),
                    false
                )
            }
        }

        if (!player.offhandItem.isEmpty) {
            val isLeftHanded = player.mainArm == HumanoidArm.LEFT
            val offhandX = if (isLeftHanded) screenCenter + HALF_HOTBAR_WIDTH + 10 else screenCenter - HALF_HOTBAR_WIDTH - 26

            hud.invokeExtractSlot(
                graphics,
                offhandX,
                slotY,
                deltaTracker,
                player,
                player.offhandItem,
                seed
            )
        }
        //~}
    }

    private fun drawAttackIndicator(
        graphics: GuiGraphicsExtractor,
        player: Player,
        screenCenter: Int,
        yInt: Int
    ) {
        val attackStrengthScale = player.getAttackStrengthScale(0f)
        if (attackStrengthScale >= 1f) return

        val isLeftHanded = player.mainArm == HumanoidArm.LEFT
        val indicatorX = if (isLeftHanded) screenCenter - HALF_HOTBAR_WIDTH - 22 else screenCenter + HALF_HOTBAR_WIDTH + 6
        val progress = (attackStrengthScale * 19f).toInt()

        //? if 1.21.1
        //com.mojang.blaze3d.systems.RenderSystem.enableBlend()

        graphics.blitSprite(
            //? if >=1.21.8 {
            net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
            //?} elif >=1.21.4
            //net.minecraft.client.renderer.rendertype.RenderType::guiTextured,
            HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE,
            indicatorX,
            yInt + 2,
            18,
            18
        )

        graphics.blitSprite(
            //? if >=1.21.8 {
            net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
            //?} elif >=1.21.4
            //net.minecraft.client.renderer.rendertype.RenderType::guiTextured,
            HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE,
            18,
            18,
            0,
            18 - progress,
            indicatorX,
            yInt + 2 + 18 - progress,
            18,
            progress
        )

        //? if 1.21.1
        //com.mojang.blaze3d.systems.RenderSystem.disableBlend()
    }
    //?}

    private fun lerp(start: Float, end: Float, delta: Float): Float {
        return start + (end - start) * delta.coerceIn(0f, 1f)
    }

    private fun applyAlpha(argb: Int, alphaMultiplier: Float): Int {
        if (alphaMultiplier >= 1f) return argb
        val a = ((argb ushr 24) * alphaMultiplier).toInt()
        return (argb and 0x00FFFFFF) or (a shl 24)
    }
    //? if 1.8.9 {
    /*

    private var cachedHeight = 0
    private var cachedWidth = 0
    private data class BlackBarData(var x: Float, var y: Int, var hiding: Boolean = false, var lastSlot: Int = 10)
    private var data: BlackBarData? = null
    private val texPath = Identifier("textures/gui/widgets.png")
    private var firstTime = true
    private var entityplayer: PlayerEntity? = null
    private var timer: Timer? = null

    private val timerTask: ActionListener = ActionListener {
        if (cachedHeight != mc.window.guiScaledHeight|| cachedWidth != mc.window.guiScaledWidth) {
            data = BlackBarData(-1.0F, mc.window.guiScaledHeight - 22)
            cachedHeight = mc.window.guiScaledHeight
            cachedWidth = mc.window.guiScaledWidth
        }
        if (entityplayer != null && mc.player != null && mc.world != null) {
            data?.let {
                val scaledHeight = mc.window.guiScaledHeight
                val scaledWidth = mc.window.guiScaledWidth
                it.hiding = mc.screen is ChatScreen
                if (it.hiding) {
                    it.y = lerp(it.y.toFloat(), scaledHeight.toFloat() + 2, (mc as MinecraftAccessor).timer.partialTick / 4).toInt()
                } else if (it.y != scaledHeight - 21) {
                    it.y = lerp(it.y.toFloat(), scaledHeight.toFloat() - 22, (mc as MinecraftAccessor).timer.partialTick / 4).toInt()
                }
                if (it.lastSlot != entityplayer!!.inventory.selectedSlot) {
                    if (scaledWidth / 2 - 91F + entityplayer!!.inventory.selectedSlot * 20 != it.x) {
                        it.x = lerp(
                            it.x,
                            scaledWidth / 2 - 91F + entityplayer!!.inventory.selectedSlot * 20,
                            (mc as MinecraftAccessor).timer.partialTick / 4
                        )
                    } else {
                        it.lastSlot = entityplayer!!.inventory.selectedSlot
                    }
                } else {
                    it.lastSlot = entityplayer!!.inventory.selectedSlot
                    it.x = scaledWidth / 2 - 91F + entityplayer!!.inventory.selectedSlot * 20
                }
            }
        }
    }

    fun initialize() {
        setTimer()
    }

    fun setTimer() {
        Multithreading.submit {
            timer?.stop()
            timer = Timer(50, timerTask)
            timer?.start()
        }
    }

    fun render() {
        data?.let {
            entityplayer = mc.camera as PlayerEntity
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)
            mc.textureManager.bind(texPath)
            if (firstTime) {
                firstTime = false
                it.x = 0F
                it.y = mc.window.guiScaledHeight - 22
                it.hiding = false
            }
            if (RedactionConfig.blackbarColor.alpha != 0) {
                drawRectEnhanced(0, it.y, mc.window.guiScaledWidth, 22, RedactionConfig.blackbarColor.argb)
            }
            if (RedactionConfig.blackbarItemColor.alpha != 0) {
                drawRectangle(
                    it.x,
                    it.y.toFloat(), 22F, 22F, Color.black // shi idk man
                )
            }
        }
    }

    fun drawRectangle(xPosition: Float, yPosition: Float, width: Float, height: Float, colour: Color) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture()
        GlStateManager.blendFuncSeparate(770, 771, 1, 0)
        val worldrenderer = Tesselator.getInstance().buffer
        GlStateManager.color4f(colour.red / 255.0f, colour.green / 255.0f, colour.blue / 255.0f, colour.alpha / 255.0f)
        worldrenderer.begin(7, DefaultVertexFormat.POSITION)
        worldrenderer.vertex(xPosition.toDouble(), (yPosition + height).toDouble(), 0.0).end()
        worldrenderer.vertex((xPosition + width).toDouble(), (yPosition + height).toDouble(), 0.0).end()
        worldrenderer.vertex((xPosition + width).toDouble(), yPosition.toDouble(), 0.0).end()
        worldrenderer.vertex(xPosition.toDouble(), yPosition.toDouble(), 0.0).end()
        Tesselator.getInstance().end()
        GlStateManager.enableTexture()
        GlStateManager.disableBlend()
        GlStateManager.bindTexture(0)
        GlStateManager.color4f(1f, 1f, 1f, 1f)
    }

    fun drawRectEnhanced(x: Int, y: Int, width: Int, height: Int, color: Int) {
        GuiElement.fill(x, y, width + x, height + y, color)
    }


     *///?}
}
