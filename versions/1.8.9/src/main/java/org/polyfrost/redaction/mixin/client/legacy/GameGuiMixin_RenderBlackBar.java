package org.polyfrost.redaction.mixin.client.legacy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.render.Window;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.client.render.platform.Lighting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.BlackBar;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameGui.class)
public abstract class GameGuiMixin_RenderBlackBar {
    @Shadow protected abstract void renderItemSlot(int slot, int x, int z, float tickDelta, PlayerEntity player);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void renderBlackBar(Window window, float tickDelta, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getBlackbar()) {
            Entity camera = this.minecraft.getCamera();
            if (!(camera instanceof PlayerEntity)) return;
            PlayerEntity player = (PlayerEntity) camera;
            ci.cancel();

            BlackBar.INSTANCE.render(player);

            GlStateManager.enableDepthTest();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            Lighting.turnOnGui();

            int scaledWidth = (int) window.getScaledWidth();
            int scaledHeight = (int) window.getScaledHeight();
            for (int j = 0; j < 9; ++j) {
                int x = scaledWidth / 2 - 90 + j * 20 + 2;
                int z = scaledHeight - 16 - 3;
                renderItemSlot(j, x, z, tickDelta, player);

                if (RedactionConfig.INSTANCE.getBlackbarSlotNumbers()) {
                    GlStateManager.disableDepthTest();
                    GlStateManager.blendFuncSeparate(775, 769, 1, 0);
                    this.minecraft.textRenderer.draw(
                            String.valueOf(j + 1),
                            x,
                            z,
                            0xFFFFFFFF,
                            false
                    );
                    GlStateManager.blendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.enableDepthTest();
                }
            }

            Lighting.turnOff();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
}
