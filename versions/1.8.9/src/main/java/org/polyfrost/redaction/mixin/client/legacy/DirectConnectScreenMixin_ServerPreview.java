package org.polyfrost.redaction.mixin.client.legacy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.multiplayer.DirectConnectScreen;
import net.minecraft.client.gui.screen.menu.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ServerListEntryWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.options.ServerListEntry;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.ServerManager;
import org.polyfrost.redaction.mixin.client.legacy.accessor.ServerListEntryWidgetAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectConnectScreen.class)
public class DirectConnectScreenMixin_ServerPreview extends Screen {
    @Shadow @Final private Screen parent;
    @Shadow private TextFieldWidget serverField;

    @Unique private ServerListEntryWidget redaction$serverPreview;

    @Inject(method = "init", at = @At("TAIL"))
    private void initServerPreview(CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview()) {
            MultiplayerScreen screen;
            if (this.parent instanceof MultiplayerScreen multiplayerScreen) {
                screen = multiplayerScreen;
            } else {
                screen = new MultiplayerScreen(this.parent);
            }
            String ip = this.serverField.getText();
            String name = !ip.isEmpty() ? ServerManager.getServerName(ip) : "Server Preview";
            redaction$serverPreview = ServerListEntryWidgetAccessor.create(
                    screen,
                    new ServerListEntry(name, ip, Minecraft.getInstance().isLocalServer())
            );
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render()V"))
    private void drawServerPreview(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview() && redaction$serverPreview != null) {
            ServerListEntryWidgetAccessor accessor = (ServerListEntryWidgetAccessor) redaction$serverPreview;
            ServerListEntry entry = accessor.getEntry();
            String text = this.serverField.getText();
            if (!entry.ip.equals(text)) {
                entry.ip = text;
                entry.name = ServerManager.getServerName(text);
                entry.setIcon(null);
            }
            redaction$serverPreview.render(0, this.width / 2 - 100, 140, 200, 24, mouseX, mouseY, false);
        }
    }
}
