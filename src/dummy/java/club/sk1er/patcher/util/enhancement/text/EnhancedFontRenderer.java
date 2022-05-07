package club.sk1er.patcher.util.enhancement.text;

import java.util.ArrayList;
import java.util.List;

public abstract class EnhancedFontRenderer {
    public static List<EnhancedFontRenderer> getInstances() {
        return new ArrayList<>();
    }
    public abstract void invalidateAll();
}
