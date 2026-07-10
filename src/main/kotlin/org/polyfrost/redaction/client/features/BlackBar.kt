package org.polyfrost.redaction.client.features

import net.minecraft.client.AttackIndicatorStatus
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.ChatScreen
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.redaction.client.RedactionConfig
import org.polyfrost.redaction.mixin.client.accessor.HudAccessor

object BlackBar {
    private val HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background")
    private val HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress")

    private var currentX = -1f
    private var currentY = -1f

    @JvmStatic
    fun renderBlackBar(graphics: GuiGraphicsExtractor, deltaTracker: DeltaTracker, player: Player) {
        val screenCenter = graphics.guiWidth() / 2
        //~ if <=1.21.4 'selectedSlot' -> 'selected'
        val targetX = screenCenter - 91f + player.inventory.selectedSlot * 20f
        //~ if <26.2 'gui.screen()' -> 'screen'
        val shouldHide = mc.gui.screen() is ChatScreen
        val targetY = if (shouldHide) graphics.guiHeight() + 2f else graphics.guiHeight() - 22f

        if (currentX == -1f) currentX = targetX
        if (currentY == -1f) currentY = targetY

        val partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true)
        val lerpFactor = partialTicks / 4f

        currentX = lerp(currentX, targetX, lerpFactor)
        currentY = lerp(currentY, targetY, lerpFactor)

        val yInt = currentY.toInt()

        if (!shouldHide || currentY < graphics.guiHeight()) {
            drawBackground(graphics, currentX.toInt(), yInt)
        }

        //~ if <26.2 'gui.hud' -> 'gui'
        val hud = mc.gui.hud as HudAccessor
        val slotY = yInt + 3

        drawSlots(graphics, hud, player, deltaTracker, screenCenter, slotY)

        if (mc.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
            drawAttackIndicator(graphics, player, screenCenter, yInt)
        }
    }

    private fun drawBackground(graphics: GuiGraphicsExtractor, xInt: Int, yInt: Int) {
        graphics.fill(
            0,
            yInt,
            graphics.guiWidth(),
            yInt + 22,
            RedactionConfig.blackbarColor.argb
        )

        graphics.fill(
            xInt,
            yInt,
            xInt + 22,
            yInt + 22,
            RedactionConfig.blackbarItemColor.argb
        )
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
            val slotX = screenCenter - 90 + i * 20 + 2

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
            val offhandX = if (isLeftHanded) screenCenter + 91 + 10 else screenCenter - 91 - 26

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
        val indicatorX = if (isLeftHanded) screenCenter - 91 - 22 else screenCenter + 91 + 6
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

    private fun lerp(start: Float, end: Float, delta: Float): Float {
        return start + (end - start) * delta.coerceIn(0f, 1f)
    }
}
