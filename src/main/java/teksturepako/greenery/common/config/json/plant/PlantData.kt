package teksturepako.greenery.common.config.json.plant

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import teksturepako.greenery.common.config.json._json
import teksturepako.greenery.common.config.json.canDecode
import teksturepako.greenery.common.config.json.toSnakeCase
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText
import kotlin.io.path.walk

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class PlantData(
    @Transient var name: String = "",
    val maxAge: Int? = null, // Backward compat
    val canGrow: Boolean = true,

    val worldGen: List<String> = listOf(),
    val drops: List<String> = listOf(),
    val allowedSoils: List<String>? = null, // Backward compat
    val soil: List<String>? = null, // Backward compat
    val compatibleFluids: List<String> = listOf(),
    val hasTintIndex: Boolean = false,
    val hasOffset: Boolean = true,
    val isSolid: Boolean = false,
    val isHarmful: Boolean = false
)
{
    companion object
    {
        fun decodeFromPath(path: Path): PlantData
        {
            val data = _json.decodeFromString<PlantData>(path.readText())

            data.name = path.nameWithoutExtension.toSnakeCase()

            return data
        }
    }
}

@OptIn(ExperimentalPathApi::class)
inline fun Path.decodePlantDataRecursive(action: (PlantData) -> Unit)
{
    for (file in this.walk()) if (file.canDecode())
    {
        action(PlantData.decodeFromPath(file))
    }
}