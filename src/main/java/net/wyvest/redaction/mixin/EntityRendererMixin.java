package net.wyvest.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.wyvest.redaction.Redaction;
import net.wyvest.redaction.config.RedactionConfig;
import org.lwjgl.util.glu.Project;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DefaultAnnotationParam")
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Shadow private Minecraft mc;
    @Shadow private float farPlaneDistance;
    @Shadow protected abstract float getFOVModifier(float partialTicks, boolean useFOVSetting);

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;loadIdentity()V", shift = At.Shift.AFTER, ordinal = 0))
    private void modifyFov(float partialTicks, int xOffset, CallbackInfo ci) {
        setOverrideHand();
    }

    @Dynamic("OptiFine")
    @Inject(method = {"renderHand*"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;loadIdentity()V", shift = At.Shift.AFTER, ordinal = 0, remap = true), remap = false)
    private void modifyFov(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        setOverrideHand();
    }

    private void setOverrideHand() {
        Redaction.INSTANCE.setOverrideHand(true);
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V"))
    private void setHookTrue(float partialTicks, int xOffset, CallbackInfo ci) {
        setHookTrue();
    }

    @Dynamic("OptiFine")
    @Inject(method = "renderHand*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", remap = true), remap = false)
    private void setHookTrue(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        setHookTrue();
    }

    private void setHookTrue() {
        Redaction.INSTANCE.setRenderingHand(true);
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", shift = At.Shift.AFTER))
    private void setHookFalse(float partialTicks, int xOffset, CallbackInfo ci) {
        setHookFalse();
    }

    @Dynamic("OptiFine")
    @Inject(method = "renderHand*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", shift = At.Shift.AFTER, remap = true), remap = false)
    private void setHookFalse(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        setHookFalse();
    }

    private void setHookFalse() {
        Redaction.INSTANCE.setRenderingHand(false);
    }

    @Inject(method = "renderHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I", opcode = Opcodes.GETFIELD, ordinal = 1))
    private void resetFOV(float partialTicks, int xOffset, CallbackInfo ci) {
        resetFOV(partialTicks, xOffset);
    }

    @Dynamic("OptiFine")
    @Inject(method = "renderHand*", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I", opcode = Opcodes.GETFIELD, ordinal = 1, remap = true), remap = false)
    private void resetFOV(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        resetFOV(f, n);
    }

    private void resetFOV(float partialTicks, int xOffset) {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        if (mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(xOffset * 2 - 1)) * 0.1F, 0.0F, 0.0F);
        }
        Project.gluPerspective(getFOVModifier(partialTicks, false), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 70.0F, ordinal = 0))
    private float modifyFOV(float constant) {
        if (Redaction.INSTANCE.getOverrideHand() && RedactionConfig.INSTANCE.getCustomHandFOV()) {
            Redaction.INSTANCE.setOverrideHand(false);
            return RedactionConfig.INSTANCE.getHandFOV();
        }
        return constant;
    }
}
