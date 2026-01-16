package org.polyfrost.redaction.mixin.client;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public interface Mixin_OnlineServerEntryAccessor {
    @Invoker("<init>")
    static ServerSelectionList.OnlineServerEntry create(ServerSelectionList serverSelectionList, JoinMultiplayerScreen joinMultiplayerScreen, ServerData serverData) {
        throw new AssertionError();
    }
}

