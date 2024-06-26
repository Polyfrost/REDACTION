package org.polyfrost.redaction.mixin;

import org.polyfrost.redaction.Redaction;
import org.polyfrost.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "club.sk1er.patcher.hooks.EntityRendererHook", remap = false)
public class PatcherEntityRendererHookMixin {
    @Dynamic("Patcher")
    @ModifyConstant(method = "getHandFOVModifier", constant = @Constant(floatValue = 70F, ordinal = 0))
    private static float modifyFOV(float constant) {
        if (Redaction.INSTANCE.getOverrideHand() && RedactionConfig.INSTANCE.getCustomHandFOV()) {
            Redaction.INSTANCE.setOverrideHand(false);
            return RedactionConfig.INSTANCE.getHandFOV();
        }
        return constant;
    }
}
