package net.wyvest.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.wyvest.redaction.config.RedactionConfig;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Shadow protected abstract float getFOVModifier(float partialTicks, boolean useFOVSetting);

    @Shadow private Minecraft mc;

    @Shadow private float farPlaneDistance;

    private boolean override = false;

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;getFOVModifier(FZ)F"))
    private void modifyFov(float partialTicks, int xOffset, CallbackInfo ci) {
        override = true;
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;enableLightmap()V"))
    private void redirectLightmap(EntityRenderer instance) {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            instance.enableLightmap();
        }
    }

    @Redirect(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;disableLightmap()V"))
    private void redirectLightmap2(EntityRenderer instance) {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            instance.disableLightmap();
        }
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V", shift = At.Shift.AFTER))
    private void resetFOV(float partialTicks, int xOffset, CallbackInfo ci) {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(xOffset * 2 - 1)) * 0.1F, 0.0F, 0.0F);
        }
        Project.gluPerspective(getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
    }

    @ModifyConstant(method = "getFOVModifier", constant = @Constant(floatValue = 70.0F, ordinal = 0))
    private float modifyFOV(float constant) {
        if (override && RedactionConfig.INSTANCE.getCustomHandFOV()) {
            override = false;
            return RedactionConfig.INSTANCE.getHandFOV();
        } else {
            return constant;
        }
    }
}
