package teksturepako.greenery.common.config.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class PlantData(
        @Transient var name: String = "",
        val maxAge: Int,
        var canGrow: Boolean = true,

        var worldGen: MutableList<String> = ArrayList(),
        var drops: MutableList<String> = ArrayList(),
        var compatibleFluids: MutableList<String> = ArrayList(),
        var hasTintIndex: Boolean = false,
        var hasOffset: Boolean = true,
        var isSolid: Boolean = false,
        var isHarmful: Boolean = false
)