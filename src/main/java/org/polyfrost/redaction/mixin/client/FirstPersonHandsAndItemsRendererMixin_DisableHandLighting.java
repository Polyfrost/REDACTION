package org.polyfrost.redaction.mixin.client;

//~ if <26.3 'FirstPersonHandsAndItemsRenderer' -> 'ItemInHandRenderer'
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import org.polyfrost.redaction.client.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//~ if <26.3 'FirstPersonHandsAndItemsRenderer' -> 'ItemInHandRenderer'
@Mixin(FirstPersonHandsAndItemsRenderer.class)
abstract class FirstPersonHandsAndItemsRendererMixin_DisableHandLighting {
    // Packed light with max block and sky light, same value as vanilla's full bright constant
    @Unique private static final int FULL_BRIGHT = 0xF000F0;

    //~ if <26.2 'submitArmWithItem' -> 'renderArmWithItem'
    @ModifyVariable(method = "submitArmWithItem", at = @At("HEAD"), argsOnly = true)
    private int disableHandLighting(int packedLight) {
        if (RedactionConfig.INSTANCE.getModEnabled() && RedactionConfig.INSTANCE.getDisableHandLighting()) {
            return FULL_BRIGHT;
        }

        return packedLight;
    }
}
