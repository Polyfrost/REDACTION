package org.polyfrost.redaction.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.BlackBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//~ if <26.2 'Hud' -> 'Gui'
@Mixin(net.minecraft.client.gui.Hud.class)
abstract class HudMixin_RenderBlackBar {
    @Shadow protected abstract @Nullable Player getCameraPlayer();

    //~ if <26.2 'Hud' -> 'Gui' {
    @WrapOperation(
            //~ if <26.1 'extract' -> 'render' {
            method = "extractHotbarAndDecorations",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Hud;extractItemHotbar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
            )
            //~}
    )
    private void renderBlackBar(net.minecraft.client.gui.Hud instance, GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, Operation<Void> original) {
        if (RedactionConfig.INSTANCE.getBlackbar()) {
            Player player = this.getCameraPlayer();
            if (player == null) return;
            BlackBar.renderBlackBar(graphics, deltaTracker, player);
        } else {
            original.call(instance, graphics, deltaTracker);
        }
    }
    //~}
}
