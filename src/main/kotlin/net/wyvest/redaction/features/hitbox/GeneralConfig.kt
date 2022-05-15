package net.wyvest.redaction.features.hitbox

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

data class GeneralConfig(
    @SerializedName("hitbox_width") var hitboxWidth: Int = 1,
    @SerializedName("force_hitbox") var forceHitbox: Boolean = false,
    @SerializedName("accurate_hitbox") var accurateHitbox: Boolean = true,
    @SerializedName("dashed_hitbox") var dashedHitbox: Boolean = false,
    @SerializedName("dashed_factor") var dashedFactor: Int = 6
) {
    companion object {
        @JvmStatic var config by Delegates.notNull<GeneralConfig>()
        internal set
    }
}