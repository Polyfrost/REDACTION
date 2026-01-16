package org.polyfrost.redaction.mixin.client;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JoinMultiplayerScreen.class)
public interface Mixin_JoinMultiplayerScreenAccessor {
    @Accessor("serverSelectionList")
    ServerSelectionList getServerSelectionList();
}

