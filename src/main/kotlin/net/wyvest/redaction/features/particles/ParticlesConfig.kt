package net.wyvest.redaction.features.particles

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

data class ParticlesConfig(
    @SerializedName("fade") var fadeParticles: Boolean = true,
) {
    companion object {
        @JvmStatic var config by Delegates.notNull<ParticlesConfig>()
            internal set
    }
}