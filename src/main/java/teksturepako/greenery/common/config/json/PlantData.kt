package teksturepako.greenery.common.config.json

import kotlinx.serialization.Serializable

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class PlantData(
        internal var name: String = "",
        var worldGen: MutableList<String> = ArrayList(),
        var drops: MutableList<String> = ArrayList(),
        var isSolid: Boolean = false,
        var isHarmful: Boolean = false
)