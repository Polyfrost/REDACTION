package net.wyvest.redaction.mixin;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.BlockPos;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "club.sk1er.oldanimations.AnimationHandler", remap = false)
public class Sk1erAnimationHandlerMixin {

    @Dynamic("Old Animations")
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;enableStandardItemLighting()V", remap = true))
    private void cancelLighting() {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Dynamic("Old Animations")
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V", remap = true))
    private void resetLighting() {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.disableStandardItemLighting();
        }
    }

    @Dynamic("Old Animations")
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OpenGlHelper;setLightmapTextureCoords(IFF)V", remap = true))
    private void cancelLightMap(int target, float p_77475_1_, float p_77475_2_) {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            OpenGlHelper.setLightmapTextureCoords(target, p_77475_1_, p_77475_2_);
        }
    }

    @Dynamic("Old Animations")
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getCombinedLight(Lnet/minecraft/util/BlockPos;I)I", remap = true))
    private int cancelLightProcessing(WorldClient world, BlockPos pos, int lightValue) {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            return world.getCombinedLight(pos, lightValue);
        } else {
            return -1;
        }
    }
}
