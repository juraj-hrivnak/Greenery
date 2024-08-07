package teksturepako.greenery.common.config.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class PlantData(
        @Transient var name: String = "",
        val maxAge: Int? = null, // Backward compat
        var canGrow: Boolean = true,

        var worldGen: List<String> = listOf(),
        var drops: List<String> = listOf(),
        val allowedSoils: List<String>? = null, // Backward compat
        var compatibleFluids: List<String> = listOf(),
        var hasTintIndex: Boolean = false,
        var hasOffset: Boolean = true,
        var isSolid: Boolean = false,
        var isHarmful: Boolean = false
)