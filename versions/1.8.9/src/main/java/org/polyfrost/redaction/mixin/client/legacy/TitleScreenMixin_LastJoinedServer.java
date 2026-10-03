package org.polyfrost.redaction.mixin.client.legacy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.menu.TitleScreen;
import net.minecraft.client.gui.screen.menu.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.ServerListEntry;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.ServerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin_LastJoinedServer extends net.minecraft.client.gui.screen.Screen {
    @Inject(method = "initWidgetsNormal", at = @At("TAIL"))
    private void onInitWidgetsNormal(int height, int offset, CallbackInfo ci) {
        String ip = RedactionConfig.INSTANCE.getLastServerIP();
        if (RedactionConfig.INSTANCE.getLastServerJoined() && !ip.trim().isEmpty()) {
            int j = this.height / 4 + 48;
            buttons.add(new ButtonWidget(45678998, this.width / 2 - 50, j + 112, 100, 20, ip));
        }
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    private void onButtonClicked(ButtonWidget button, CallbackInfo ci) {
        String ip = RedactionConfig.INSTANCE.getLastServerIP();
        if (button.id == 45678998 && RedactionConfig.INSTANCE.getLastServerJoined() && !ip.trim().isEmpty()) {
            ServerListEntry entry = new ServerListEntry(ServerManager.getServerName(ip), ip, Minecraft.getInstance().isLocalServer());
            this.minecraft.setScreen(new ConnectScreen(this, this.minecraft, entry));
        }
    }
}
