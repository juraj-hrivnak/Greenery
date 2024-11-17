package teksturepako.greenery.common.config.json.arbBlock

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.block.state.IBlockState
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.ForgeRegistries
import teksturepako.greenery.common.config.json._json
import teksturepako.greenery.common.config.json.canDecode
import teksturepako.greenery.common.config.json.toSnakeCase
import teksturepako.greenery.common.config.parser.SoilParser
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readText
import kotlin.io.path.walk

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class ArbBlockData(
    @Transient var name: String = "",
    private val blocks: List<String> = listOf(),
    val worldGen: List<String> = listOf(),
    val allowedSoils: List<String> = listOf(),
    val soil: List<String>? = null,
)
{
    @Transient var blockStates: List<IBlockState> = listOf()
    @Transient lateinit var soilFunc: (IBlockState) -> Boolean

    private fun parseBlockStates(): List<IBlockState>
    {
        return this.blocks.mapNotNull { blockString ->

            val arg = blockString.filter { !it.isWhitespace() }.trim().split(":")

            val namespace = arg.getOrNull(0) ?: return@mapNotNull null
            val path = arg.getOrNull(1) ?: return@mapNotNull null
            val metaString = arg.getOrNull(2)

            val block = ForgeRegistries.BLOCKS.getValue(ResourceLocation(namespace, path)) ?: return@mapNotNull null

            val meta = metaString?.toIntOrNull()

            if (meta != null)
            {
                @Suppress("DEPRECATION")
                block.blockState.block.getStateFromMeta(meta)
            }
            else
            {
                block.blockState.baseState
            }
        }.asReversed()
    }

    companion object
    {
        fun decodeFromPath(path: Path): ArbBlockData
        {
            val data = _json.decodeFromString<ArbBlockData>(path.readText())

            data.name = path.nameWithoutExtension.toSnakeCase()
            data.blockStates = data.parseBlockStates()

            data.soilFunc = data.soil?.let { soils -> SoilParser.parse(soils) }
                ?: SoilParser.parse(SoilParser.fromAllowedSoils(data.allowedSoils))

            return data
        }
    }
}

@OptIn(ExperimentalPathApi::class)
inline fun Path.decodeArbBlockDataRecursive(action: (ArbBlockData) -> Unit)
{
    for (file in this.walk()) if (file.canDecode())
    {
        action(ArbBlockData.decodeFromPath(file))
    }
}
