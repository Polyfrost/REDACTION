package net.wyvest.redaction.hook;

import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public interface EntityBreakingFXHook {
    @Nullable
    Item getItem();
    boolean isProjectile();
}
