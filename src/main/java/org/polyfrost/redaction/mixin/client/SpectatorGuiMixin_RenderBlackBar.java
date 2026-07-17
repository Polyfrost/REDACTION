package org.polyfrost.redaction.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.spectator.categories.SpectatorPage;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.BlackBar;
import org.polyfrost.redaction.mixin.client.accessor.SpectatorGuiAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpectatorGui.class)
abstract class SpectatorGuiMixin_RenderBlackBar {
    //~ if <26.1 'extract' -> 'render' {
    @WrapOperation(
        method = "extractHotbar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;extractPage(Lnet/minecraft/client/gui/GuiGraphicsExtractor;FIILnet/minecraft/client/gui/spectator/categories/SpectatorPage;)V"
        )
    )
    //~}
    private void renderSpectatorBlackBar(
        SpectatorGui instance,
        GuiGraphicsExtractor graphics,
        float alpha,
        int screenCenter,
        int y,
        SpectatorPage page,
        Operation<Void> original
    ) {
        if (RedactionConfig.INSTANCE.getBlackbar()) {
            BlackBar.extractSpectatorBlackBar(graphics, alpha, screenCenter, y, page, (SpectatorGuiAccessor) this);
        } else {
            original.call(instance, graphics, alpha, screenCenter, y, page);
        }
    }
}
