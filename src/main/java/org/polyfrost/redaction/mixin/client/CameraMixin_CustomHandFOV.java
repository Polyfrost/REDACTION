package org.polyfrost.redaction.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//~ if <26.1 'Camera' -> 'renderer.GameRenderer'
import net.minecraft.client.Camera;
import org.polyfrost.redaction.client.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//~ if <26.1 'Camera' -> 'GameRenderer'
@Mixin(Camera.class)
abstract class CameraMixin_CustomHandFOV {
    //~ if <26.1 'calculateHudFov' -> 'getFov'
    //~ if 1.21.1 'float' -> 'double' {
    @ModifyExpressionValue(method = "calculateHudFov", at = @At(value = "CONSTANT", args = "floatValue=70"))
    private float modifyFov(float original) {
        if (RedactionConfig.INSTANCE.getModEnabled() && RedactionConfig.INSTANCE.getCustomHandFOV()) {
            return RedactionConfig.INSTANCE.getHandFOV();
        }

        return original;
    }
    //~}
}
