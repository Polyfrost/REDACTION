package org.polyfrost.redaction.mixin.client.accessor;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpectatorGui.class)
public interface SpectatorGuiAccessor {
    @Invoker
    //~ if <26.1 'invokeExtractSlot' -> 'invokeRenderSlot'
    void invokeExtractSlot(GuiGraphicsExtractor graphics, int slot, int x, float y, float alpha, SpectatorMenuItem item);
}
