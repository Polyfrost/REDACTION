package org.polyfrost.redaction.mixin.client.legacy;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.model.block.BakedModel;
import net.minecraft.client.resource.model.ModelTransformations;
import net.minecraft.item.ItemStack;
import org.polyfrost.redaction.client.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin_DisableHandLighting {
    @Unique private boolean renderingFirstPerson = false;

    @Inject(
            method = "renderItemInHand(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/BakedModel;Lnet/minecraft/client/resource/model/ModelTransformations$Type;)V",
            at = @At("HEAD")
    )
    private void markFirstPerson(ItemStack stack, BakedModel model, ModelTransformations.Type type, CallbackInfo ci) {
        renderingFirstPerson = type == ModelTransformations.Type.FIRST_PERSON;
    }

    @Inject(
            method = "renderItemInHand(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/BakedModel;Lnet/minecraft/client/resource/model/ModelTransformations$Type;)V",
            at = @At("RETURN")
    )
    private void clearFirstPerson(ItemStack stack, BakedModel model, ModelTransformations.Type type, CallbackInfo ci) {
        renderingFirstPerson = false;
    }

    // The glint turns lighting back on when it finishes, which would light the rest of the held item
    @WrapWithCondition(method = "renderEnchantmentGlint", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;enableLighting()V"))
    private boolean keepLightingOff() {
        return !(renderingFirstPerson && RedactionConfig.INSTANCE.getModEnabled() && RedactionConfig.INSTANCE.getDisableHandLighting());
    }
}
