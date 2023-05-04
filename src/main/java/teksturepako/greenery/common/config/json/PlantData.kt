package teksturepako.greenery.common.config.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlantData(
        @Transient
        var name: String = "",
        var worldGen: MutableList<String> = ArrayList(),
        var drops: MutableList<String> = ArrayList(),
        var hasTintIndex: Boolean = false,
        var isSolid: Boolean = false,
        var isHarmful: Boolean = false
)