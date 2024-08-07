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
class WorldGenParser(private val currentConfig: String, allConfigs: List<String>)
{
    /**
     * Filters & splits input string on "|".
     * @return list of split strings.
     */
    private fun splitInput(input: String): List<String> = if (input.isNotEmpty())
    {
        input.filter { !it.isWhitespace() }.trim().split("|")
    }
    else emptyList()

    /**
     * Splits input string on ":".
     */
    private fun getConditionInput(splitInput: List<String>): List<String> = if (splitInput.isNotNull(1))
    {
        splitInput[1].split(":")
    }
    else emptyList()

    /** A list of biome resource locations. */
    val biomesResLocs: List<ResourceLocation> = allConfigs.mapNotNull { input ->
        val configInput = getConditionInput(splitInput(input))
        if ("biome" inNotNull configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
        {
            ResourceLocation(configInput[1], configInput[2])
        }
        else null
    }

    /** A list of valid biomes. */
    val biomes: List<Biome> = allConfigs.mapNotNull { input ->
        val condition = getConditionInput(splitInput(input))
        if ("biome" inNotNull condition[0] && condition.isNotNull(1) && condition.isNotNull(2))
        {
            ForgeRegistries.BIOMES.getValue(ResourceLocation(condition[1], condition[2]))
        }
        else null
    }

    /** A list of valid BiomeDictionary Types. */
    val types: List<BiomeDictionary.Type> = allConfigs.mapNotNull { input ->
        val condition = getConditionInput(splitInput(input))
        if ("type" inNotNull condition[0] && condition.isNotNull(1))
        {
            BiomeDictionary.Type.getType(condition[1])
        }
        else null
    }

    /** The valid dimension ID for this configuration. */
    val dimension: Int = splitInput(currentConfig).firstOrNull()?.toIntOrNull() ?: 0

    val dimensions: List<Int> = allConfigs.mapNotNull { splitInput(it).firstOrNull()?.toIntOrNull() }

    val numberOfConfigsInDimension: Int = dimensions.filter { it == dimension }.size

    /**
     * 1. Checks if the dimension is valid.
     * 2. Checks if the condition is not null.
     * 3. Gets all BiomeDictionary Types and biomes for this configuration.
     */
    fun canGenerate(biome: Biome, dimension: Int): Boolean
    {
        if (dimension != this.dimension) return false

        val condition = getConditionInput(splitInput(currentConfig)).firstOrNull() ?: return false

        return when
        {
            // Inverted
            "!" in condition ->
            {
                if ("type" in condition)
                {
                    types.none { BiomeDictionary.hasType(biome, it) }
                }
                else if ("biome" in condition)
                {
                    biomes.none { biome.registryName == it.registryName }
                }
                else false
            }
            // Not inverted
            "type" in condition -> types.any { BiomeDictionary.hasType(biome, it) }
            "biome" in condition -> biomes.any { biome.registryName == it.registryName }
            // Keywords
            "anywhere" in condition || "everywhere" in condition -> true

            else -> false
        }
    }

    val generationChance: Double = if (splitInput(currentConfig).isNotNull(2)) splitInput(currentConfig)[2].toDouble() else 0.0

    val patchAttempts: Int = if (splitInput(currentConfig).isNotNull(3))
    {
        splitInput(currentConfig)[3].toInt() / (numberOfConfigsInDimension - 1).coerceAtLeast(1)
    }
    else 0

    fun patchAttempts(multiplyBy: Int = 1): Int = if (splitInput(currentConfig).isNotNull(3))
    {
        splitInput(currentConfig)[3].toInt() * multiplyBy / (numberOfConfigsInDimension - 1).coerceAtLeast(1)
    }
    else 0

    val plantAttempts: Int = if (splitInput(currentConfig).isNotNull(4)) splitInput(currentConfig)[4].toInt() else 0
}