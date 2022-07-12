package net.wyvest.redaction.mixin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin extends GuiScreen {
    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At("TAIL"))
    protected void onGuiInit(int p_73969_1_, int p_73969_2_, CallbackInfo ci) {
        String ip = RedactionConfig.INSTANCE.getLastServerIP();
        if (RedactionConfig.INSTANCE.getLastServerJoined() && !ip.trim().isEmpty()) {
            int j = height / 4 + 48;
            buttonList.add(new GuiButton(45678998, width / 2 - 50, j + 112, 100, 20, ip));
        }
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    protected void onActionPerformed(GuiButton button, CallbackInfo ci) {
        String ip = RedactionConfig.INSTANCE.getLastServerIP();
        if (button.id == 45678998 && RedactionConfig.INSTANCE.getLastServerJoined() && !ip.trim().isEmpty()) {
            mc.displayGuiScreen(new GuiConnecting(this, mc, new ServerData(ip, ip, false)));
        }
    }
}