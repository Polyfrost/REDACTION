package net.wyvest.redaction.features.hitbox

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

data class GeneralConfig(
    @SerializedName("hitbox_width") var hitboxWidth: Int = 1,
    @SerializedName("force_hitbox") var forceHitbox: Boolean = false,
    @SerializedName("disable_for_self") var disableForSelf: Boolean = false,
    @SerializedName("accurate_hitbox") var accurateHitbox: Boolean = true
) {
    companion object {
        @JvmStatic var config by Delegates.notNull<GeneralConfig>()
        internal set
    }
}