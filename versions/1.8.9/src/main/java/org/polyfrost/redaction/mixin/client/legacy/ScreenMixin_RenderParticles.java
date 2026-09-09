package org.polyfrost.redaction.mixin.client.legacy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.game.inventory.InventoryMenuScreen;
import org.polyfrost.redaction.client.features.particles.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin_RenderParticles {
    @Inject(method = "render", at = @At("HEAD"))
    private void renderParticles(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if ((Object) this instanceof InventoryMenuScreen) {
            ParticleManager.renderParticlesLegacy(mouseX, mouseY);
        }
    }
}
