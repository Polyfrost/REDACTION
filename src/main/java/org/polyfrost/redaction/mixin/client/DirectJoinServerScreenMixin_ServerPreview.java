package org.polyfrost.redaction.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.ServerManager;
import org.polyfrost.redaction.mixin.client.accessor.JoinMultiplayerScreenAccessor;
import org.polyfrost.redaction.mixin.client.accessor.OnlineServerEntryAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectJoinServerScreen.class)
abstract class DirectJoinServerScreenMixin_ServerPreview extends Screen {
    @Shadow @Final private Screen lastScreen;
    @Shadow private EditBox ipEdit;

    @Unique private ServerData redaction$serverPreview;
    @Unique private ServerSelectionList.OnlineServerEntry redaction$serverEntry;

    protected DirectJoinServerScreenMixin_ServerPreview(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initServerPreview(CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview()) {
            createServerPreview("", "");
        }
    }

    //~ if <26.1 'extractRenderState' -> 'render'
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void drawServerPreview(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (!RedactionConfig.INSTANCE.getServerPreview() || redaction$serverPreview == null || redaction$serverEntry == null) {
            return;
        }

        if (!redaction$serverPreview.ip.equals(this.ipEdit.getValue())) {
            createServerPreview(ServerManager.getServerName(this.ipEdit.getValue()), this.ipEdit.getValue());
        }

        //? if >=26.1 {
        redaction$serverEntry.extractContent(
        //?} elif >=1.21.10 {
        /*redaction$serverEntry.renderContent(
        *///?} else
        //redaction$serverEntry.render(
                graphics,
                //? if <1.21.10 {
                /*0,
                30,
                graphics.guiWidth() / 2 - 100,
                200,
                35,
                *///?}
                mouseX,
                mouseY,
                false,
                a
        );
    }

    @Unique
    private void createServerPreview(String name, String ip) {
        redaction$serverPreview = new ServerData(name, ip, ServerData.Type.OTHER);

        if (this.lastScreen instanceof JoinMultiplayerScreen joinMultiplayerScreen) {
            redaction$serverEntry = OnlineServerEntryAccessor.create(
                    ((JoinMultiplayerScreenAccessor) joinMultiplayerScreen).getServerSelectionList(),
                    joinMultiplayerScreen,
                    redaction$serverPreview
            );
            //? if >=1.21.10 {
            redaction$serverEntry.setX(this.width / 2 - 100);
            redaction$serverEntry.setY(30);
            redaction$serverEntry.setWidth(200);
            redaction$serverEntry.setHeight(35);
            //?}
        }
    }
}
