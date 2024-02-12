@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.world

import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.fml.common.registry.ForgeRegistries
import teksturepako.greenery.common.util.Utils.inNotNull
import teksturepako.greenery.common.util.Utils.isNotNull

/**
 * World generation configuration parser
 */
class WorldGenParser(private val indexedInput: String, private val worldGenConfig: MutableList<String>)
{
    /**
     * Filters & splits input string on "|".
     * @return list of split strings.
     */
    private fun getSplitInput(string: String): List<String>
    {
        return if (string.isNotEmpty())
        {
            string.filter { !it.isWhitespace() }.trim().split("|")
        }
        else emptyList()
    }

    /**
     * Splits input string on ":".
     */
    private fun getConditionInput(splitInput: List<String>): List<String>
    {
        return if (splitInput.isNotNull(1))
        {
            splitInput[1].split(":")
        }
        else emptyList()
    }

    /** A list of biome resource locations. */
    val biomesResLocs: List<ResourceLocation> = worldGenConfig.mapNotNull { input ->
        val configInput = getConditionInput(getSplitInput(input))
        if ("biome" inNotNull configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
        {
            ResourceLocation(configInput[1], configInput[2])
        }
        else null
    }

    /** A list of valid biomes. */
    val biomes: List<Biome> = worldGenConfig.mapNotNull { input ->
        val configInput = getConditionInput(getSplitInput(input))
        if ("biome" inNotNull configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
        {
            ForgeRegistries.BIOMES.getValue(ResourceLocation(configInput[1], configInput[2]))
        }
        else null
    }

    /** A list of valid BiomeDictionary Types. */
    val types: List<BiomeDictionary.Type> = worldGenConfig.mapNotNull { input ->
        val configInput = getConditionInput(getSplitInput(input))
        if ("type" inNotNull configInput[0] && configInput.isNotNull(1))
        {
            BiomeDictionary.Type.getType(configInput[1])
        }
        else null
    }

    /** The valid dimension ID for this configuration. */
    val dimension: Int = if (getSplitInput(indexedInput).isNotNull(0)) getSplitInput(indexedInput)[0].toInt() else 0

    /**
     * 1. Checks if the dimension is valid.
     * 2. Checks if the condition is not null.
     * 3. Gets all BiomeDictionary Types and biomes for this configuration.
     */
    fun canGenerate(biome: Biome, dimension: Int): Boolean
    {
        if (dimension != this.dimension) return false

        val configInput = getConditionInput(getSplitInput(indexedInput))
        if (configInput.isNotNull(0))
        {
            when
            {
                // Inverted
                "!" in configInput[0] ->
                {
                    if ("type" in configInput[0])
                    {
                        return types.none { BiomeDictionary.hasType(biome, it) }
                    }
                    else if ("biome" in configInput[0])
                    {
                        return biomes.none { biome.registryName == it.registryName }
                    }
                }
                // Not inverted
                "type" in configInput[0] -> return types.any { BiomeDictionary.hasType(biome, it) }
                "biome" in configInput[0] -> return biomes.any { biome.registryName == it.registryName }
                // Keywords
                "anywhere" in configInput[0] || "everywhere" in configInput[0] -> return true
            }
        }
        return false // Fallback to false
    }

    val generationChance: Double = if (getSplitInput(indexedInput).isNotNull(2)) getSplitInput(indexedInput)[2].toDouble() else 0.0
    val patchAttempts: Int = if (getSplitInput(indexedInput).isNotNull(3)) getSplitInput(indexedInput)[3].toInt() else 0
    val plantAttempts: Int = if (getSplitInput(indexedInput).isNotNull(4)) getSplitInput(indexedInput)[4].toInt() else 0
}