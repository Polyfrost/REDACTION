package org.polyfrost.redaction.mixin.client.legacy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import org.polyfrost.redaction.client.RedactionConfig;
import org.lwjgl.util.glu.Project;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin_CustomHandFOV {
    @Shadow protected abstract float getFov(float partialTicks, boolean useFOVSetting);

    @Shadow private float renderDistance;
    @Shadow private Minecraft minecraft;
    @Unique private boolean overrideHandFov = false;

    @Inject(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;loadIdentity()V", shift = At.Shift.AFTER, ordinal = 0))
    private void modifyFov(float partialTicks, int xOffset, CallbackInfo ci) {
        overrideHandFov = true;
    }

    @Inject(method = "renderItemInHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;perspective:I", opcode = Opcodes.GETFIELD, ordinal = 1))
    private void resetFOV(float partialTicks, int xOffset, CallbackInfo ci) {
        resetFOVMatrix(partialTicks, xOffset);
    }

    private void resetFOVMatrix(float partialTicks, int xOffset) {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        if (this.minecraft.options.anaglyph) {
            GlStateManager.translatef((float)(-(xOffset * 2 - 1)) * 0.1F, 0.0F, 0.0F);
        }
        Project.gluPerspective(getFov(partialTicks, false), (float)this.minecraft.width / (float)this.minecraft.height, 0.05F, this.renderDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (this.minecraft.options.anaglyph) {
            GlStateManager.translatef((float)(xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
    }

    @ModifyConstant(method = "getFov", constant = @Constant(floatValue = 70.0F, ordinal = 0))
    private float modifyFOV(float constant) {
        if (overrideHandFov) {
            overrideHandFov = false;
            if (RedactionConfig.INSTANCE.getCustomHandFOV()) {
                return RedactionConfig.INSTANCE.getHandFOV();
            }
        }
        return constant;
    }
}
