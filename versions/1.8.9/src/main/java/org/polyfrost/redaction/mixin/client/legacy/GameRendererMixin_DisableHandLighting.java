package org.polyfrost.redaction.mixin.client.legacy;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.render.GameRenderer;
import org.polyfrost.redaction.client.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin_DisableHandLighting {
    @WrapWithCondition(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;enableLightMap()V"))
    private boolean skipEnableLightMap(GameRenderer instance) {
        return !redaction$isEnabled();
    }

    @WrapWithCondition(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;disableLightMap()V"))
    private boolean skipDisableLightMap(GameRenderer instance) {
        return !redaction$isEnabled();
    }

    @Unique
    private static boolean redaction$isEnabled() {
        return RedactionConfig.INSTANCE.getModEnabled() && RedactionConfig.INSTANCE.getDisableHandLighting();
    }
}
