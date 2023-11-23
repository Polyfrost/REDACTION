package net.wyvest.redaction.mixin;

import cc.woverflow.onecore.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.wyvest.redaction.features.hitbox.Entity;
import net.wyvest.redaction.features.hitbox.GeneralConfig;
import net.wyvest.redaction.gui.HitboxPreviewGUI;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = RenderManager.class)
public class RenderManagerMixin {
    private boolean box = true;
    private boolean eye = true;
    private boolean line = true;
    private int hitboxColor;
    private int eyeColor;
    private int lineColor;
    private int crosshairColor;
    private boolean awaitingGUI = false;

    @Inject(method = "doRenderEntity", at = @At(value = "HEAD"))
    private void forceHitbox(net.minecraft.entity.Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable<Boolean> cir) {
        if (GeneralConfig.getConfig().getForceHitbox()) {
            if (!Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox())
                Minecraft.getMinecraft().getRenderManager().setDebugBoundingBox(true);
            return;
        }
        if (HitboxPreviewGUI.Companion.getBypassHitbox()) {
            awaitingGUI = !Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox();
            if (awaitingGUI)
                Minecraft.getMinecraft().getRenderManager().setDebugBoundingBox(true);
        } else if (awaitingGUI) {
            Minecraft.getMinecraft().getRenderManager().setDebugBoundingBox(false);
            awaitingGUI = false;
        }
    }

    @Inject(method = "renderDebugBoundingBox", at = @At("HEAD"), cancellable = true)
    private void initHitbox(net.minecraft.entity.Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (HitboxPreviewGUI.Companion.getBypassHitbox()) {
            Entity entity = HitboxPreviewGUI.Companion.getEntityToEmulate();
            box = entity.getHitboxEnabled();
            eye = entity.getEyeLineEnabled();
            line = entity.getLineEnabled();
            hitboxColor = entity.getColor();
            crosshairColor = entity.getCrosshairColor();
            eyeColor = entity.getEyeColor();
            lineColor = entity.getLineColor();
            if (GeneralConfig.getConfig().getDashedHitbox()) {
                GL11.glEnable(GL11.GL_LINE_STIPPLE);
                GL11.glLineStipple(GeneralConfig.getConfig().getDashedFactor(), (short) 0xAAAA);
            }
            GL11.glLineWidth(GeneralConfig.getConfig().getHitboxWidth());
            return;
        }
        for (Entity entity : Entity.getSortedList()) {
            if (entity.getCondition().invoke(entityIn)) {
                if ((!entity.getHitboxEnabled() && !entity.getEyeLineEnabled() && !entity.getLineEnabled())) {
                    ci.cancel();
                    return;
                }
                box = entity.getHitboxEnabled();
                eye = entity.getEyeLineEnabled();
                line = entity.getLineEnabled();
                hitboxColor = entity.getColor();
                crosshairColor = entity.getCrosshairColor();
                eyeColor = entity.getEyeColor();
                lineColor = entity.getLineColor();
                if (GeneralConfig.getConfig().getDashedHitbox()) {
                    GL11.glEnable(GL11.GL_LINE_STIPPLE);
                    GL11.glLineStipple(GeneralConfig.getConfig().getDashedFactor(), (short) 0xAAAA);
                }
                GL11.glLineWidth(GeneralConfig.getConfig().getHitboxWidth());
                return;
            }
        }
    }

    @Redirect(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;IIII)V", ordinal = 0))
    private void shouldRenderHitbox(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha, net.minecraft.entity.Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
        if (box) {
            int color = (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && Minecraft.getMinecraft().objectMouseOver.entityHit.equals(entityIn)) || (HitboxPreviewGUI.Companion.getBypassHitbox() && HitboxPreviewGUI.Companion.getCursorOverEmulatedEntity()) ? crosshairColor : hitboxColor;
            RenderGlobal.drawOutlinedBoundingBox((GeneralConfig.getConfig().getAccurateHitbox() ? boundingBox.expand(entityIn.getCollisionBorderSize(), entityIn.getCollisionBorderSize(), entityIn.getCollisionBorderSize()) : boundingBox), ColorUtils.getRed(color), ColorUtils.getGreen(color), ColorUtils.getBlue(color), alpha);
        }
    }

    @Redirect(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;IIII)V", ordinal = 1))
    private void shouldRenderLineOfSight(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha) {
        if (line) {
            RenderGlobal.drawOutlinedBoundingBox(boundingBox, ColorUtils.getRed(lineColor), ColorUtils.getGreen(lineColor), ColorUtils.getBlue(lineColor), alpha);
        }
    }

    @Redirect(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    private void redirectEyeLineDraw(Tessellator instance) {
        if (!eye) {
            instance.getWorldRenderer().finishDrawing();
            instance.getWorldRenderer().reset();
        } else {
            instance.draw();
        }
    }

    @ModifyArgs(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;color(IIII)Lnet/minecraft/client/renderer/WorldRenderer;"))
    private void modifyEyeLineColor(Args args) {
        args.set(0, ColorUtils.getRed(eyeColor));
        args.set(1, ColorUtils.getGreen(eyeColor));
        args.set(2, ColorUtils.getBlue(eyeColor));
    }

    @Inject(method = "renderDebugBoundingBox", at = @At("RETURN"))
    private void resetLineWidth(net.minecraft.entity.Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GL11.glLineWidth(1);
        if (GeneralConfig.getConfig().getDashedHitbox()) {
            GL11.glDisable(GL11.GL_LINE_STIPPLE);
        }
    }
}
