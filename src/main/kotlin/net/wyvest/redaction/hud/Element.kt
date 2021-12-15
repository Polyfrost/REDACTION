package net.wyvest.redaction.hud

import net.minecraft.client.gui.ScaledResolution

abstract class Element {
    open fun initialize() {}

    open fun render(
        res: ScaledResolution,
        partialTicks: Float
    ) {
    }

    open fun actuallyRender(): Boolean {
        return true
    }
}
