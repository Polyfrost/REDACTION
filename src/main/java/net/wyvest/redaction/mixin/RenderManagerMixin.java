package net.wyvest.redaction.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import gg.essential.handlers.RenderPlayerBypass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.wyvest.redaction.features.hitbox.Entity;
import net.wyvest.redaction.features.hitbox.GeneralConfig;
import net.wyvest.redaction.gui.HitboxPreviewGUI;
import net.wyvest.redaction.utils.ColorUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RenderManager.class)
public class RenderManagerMixin {
    private boolean didDo = false;
    private boolean box = true;
    private boolean eye = true;
    private boolean line = true;
    private int hitboxColor;
    private int eyeColor;
    private int lineColor;
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
    private void bypassEmulatedPlayerHitbox(net.minecraft.entity.Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (RenderPlayerBypass.bypass && HitboxPreviewGUI.Companion.getBypassHitbox()) {
            RenderPlayerBypass.bypass = false;
            didDo = true;
            return;
        } else {
            didDo = false;
        }
        for (Entity entity : Entity.getSortedList()) {
            if (entity.getCondition().invoke(entityIn) || HitboxPreviewGUI.Companion.getBypassHitbox()) {
                if (HitboxPreviewGUI.Companion.getBypassHitbox()) {
                    entity = HitboxPreviewGUI.Companion.getEntityToEmulate();
                    box = entity.getHitboxEnabled();
                    eye = entity.getEyeLineEnabled();
                    line = entity.getLineEnabled();
                    hitboxColor = entity.getColor();
                    eyeColor = entity.getEyeColor();
                    lineColor = entity.getLineColor();
                    GL11.glLineWidth(GeneralConfig.getConfig().getHitboxWidth());
                    return;
                }
                if ((!entity.getHitboxEnabled() && !entity.getEyeLineEnabled() && !entity.getLineEnabled()) || (GeneralConfig.getConfig().getDisableForSelf() && "Self".equals(entity.getName()))) {
                    ci.cancel();
                    return;
                }
                box = entity.getHitboxEnabled();
                eye = entity.getEyeLineEnabled();
                line = entity.getLineEnabled();
                hitboxColor = entity.getColor();
                eyeColor = entity.getEyeColor();
                lineColor = entity.getLineColor();
                GL11.glLineWidth(GeneralConfig.getConfig().getHitboxWidth());
            }
        }
    }

    @WrapWithCondition(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;IIII)V", ordinal = 0))
    private boolean shouldRenderHitbox(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha) {
        return box;
    }

    @ModifyArgs(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;IIII)V", ordinal = 0))
    private void modifyHitbox(Args args, net.minecraft.entity.Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
        if (GeneralConfig.getConfig().getAccurateHitbox()) {
            args.set(0, ((AxisAlignedBB) args.get(0)).expand(entityIn.getCollisionBorderSize(), entityIn.getCollisionBorderSize(), entityIn.getCollisionBorderSize()));
        }
        args.set(1, ColorUtils.getRed(hitboxColor));
        args.set(2, ColorUtils.getGreen(hitboxColor));
        args.set(3, ColorUtils.getBlue(hitboxColor));
    }

    @WrapWithCondition(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;IIII)V", ordinal = 1))
    private boolean shouldRenderLineOfSight(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha) {
        return line;
    }

    @ModifyArgs(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;drawOutlinedBoundingBox(Lnet/minecraft/util/AxisAlignedBB;IIII)V", ordinal = 1))
    private void modifyLineOfSightColor(Args args) {
        args.set(1, ColorUtils.getRed(lineColor));
        args.set(2, ColorUtils.getGreen(lineColor));
        args.set(3, ColorUtils.getBlue(lineColor));
    }

    @WrapWithCondition(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    private boolean redirectEyeLineDraw(Tessellator instance) {
        if (!eye) {
            instance.getWorldRenderer().finishDrawing();
            instance.getWorldRenderer().reset();
        }
        return eye;
    }

    @ModifyArgs(method = "renderDebugBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;color(IIII)Lnet/minecraft/client/renderer/WorldRenderer;"))
    private void modifyEyeLineColor(Args args) {
        args.set(0, ColorUtils.getRed(eyeColor));
        args.set(1, ColorUtils.getGreen(eyeColor));
        args.set(2, ColorUtils.getBlue(eyeColor));
    }

    @Inject(method = "renderDebugBoundingBox", at = @At("RETURN"))
    private void resetEmulatedPlayerHitboxBypass(net.minecraft.entity.Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (didDo) {
            RenderPlayerBypass.bypass = true;
        }
        GL11.glLineWidth(1);
    }
}
