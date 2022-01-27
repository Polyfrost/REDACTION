package net.wyvest.redaction.mixin;

import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.wyvest.redaction.config.RedactionConfig;
import net.wyvest.redaction.features.ServerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenServerList.class)
public class GuiScreenServerListMixin extends GuiScreen {
    @Shadow @Final private GuiScreen field_146303_a;
    @Shadow private GuiTextField field_146302_g;
    private ServerListEntryNormal serverPreview;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void initServerPreview(CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview()) {
            serverPreview = new ServerListEntryNormal(((GuiMultiplayer) field_146303_a), new ServerData("", "", false));
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;drawTextBox()V"))
    private void drawServerPreview(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview() && serverPreview != null) {
            if (!serverPreview.getServerData().serverIP.equals(field_146302_g.getText())) {
                serverPreview.getServerData().serverIP = field_146302_g.getText();
                serverPreview.getServerData().serverName = ServerManager.INSTANCE.getNameOfServer(field_146302_g.getText());
                serverPreview.getServerData().field_78841_f = false;
            }
            serverPreview.drawEntry(0, width / 2 - 100, 30, 200, 35, mouseX, mouseY, false);
        }
    }
}
