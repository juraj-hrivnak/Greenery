@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.util

import biomesoplenty.api.biome.BOPBiomes
import biomesoplenty.api.biome.IExtendedBiome
import biomesoplenty.common.biome.vanilla.ExtendedBiomeWrapper
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.ForgeRegistries
import java.util.*

object WorldGenUtil
{
    fun removeBOPGenerators(world: World)
    {
        if (!Loader.isModLoaded("biomesoplenty") || world.isRemote) return

        for (biome in BOPBiomes.REG_INSTANCE.presentBiomes)
        {
            getExtendedBiome(biome).generationManager.removeGenerator("grass")
            getExtendedBiome(biome).generationManager.removeGenerator("ferns")
            getExtendedBiome(biome).generationManager.removeGenerator("double_fern")
            getExtendedBiome(biome).generationManager.removeGenerator("doublegrass")
            getExtendedBiome(biome).generationManager.removeGenerator("barley")
        }
    }

    private fun getExtendedBiome(biome: Biome): IExtendedBiome
    {
        return BOPBiomes.REG_INSTANCE.getExtendedBiome(biome) ?: let {
            val extendedBiome = ExtendedBiomeWrapper(biome)
            BOPBiomes.REG_INSTANCE.registerBiome(
                extendedBiome, extendedBiome.baseBiome.biomeName.lowercase(Locale.getDefault())
            )
        }
    }

    fun getBiomeInChunk(world: World, chunkX: Int, chunkZ: Int): Biome
    {
        return world.getBiomeForCoordsBody(BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8))
    }

    /**
     * World generation configuration parser
     */
    class Parser(private val indexedInput: String, private val worldGenConfig: MutableList<String>)
    {
        /**
         * Filters & splits input string on "|".
         * @return List of split strings.
         */
        private fun getSplitInput(string: String): List<String>
        {
            return if (string.isNotEmpty()) string.filter { !it.isWhitespace() }.trim().split("|") else emptyList()
        }

        /**
         * Splits input string on ":".
         */
        private fun getConfigInput(splitInput: List<String>): List<String>
        {
            return if (splitInput.isNotNull(1)) splitInput[1].split(":") else emptyList()
        }

        /**
         * Returns a list of biome resource locations.
         */
        fun getBiomesResLoc(): List<ResourceLocation>
        {
            val result: MutableList<ResourceLocation> = mutableListOf()

            for (input in worldGenConfig)
            {
                val configInput = getConfigInput(getSplitInput(input))
                if ("biome" inNotNull configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
                {
                    result.add(ResourceLocation(configInput[1], configInput[2]))
                }
            }
            return result
        }

        /**
         * Returns a list of biomes.
         */
        fun getBiomes(): List<Biome>
        {
            val result: MutableList<Biome> = mutableListOf()

            for (input in worldGenConfig)
            {
                val configInput = getConfigInput(getSplitInput(input))
                if ("biome" inNotNull configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
                {
                    ForgeRegistries.BIOMES.getValue(ResourceLocation(configInput[1], configInput[2]))?.let { result.add(it) }
                }
            }
            return result
        }

        /**
         * Returns a list of BiomeDictionary Types.
         */
        fun getTypes(): List<BiomeDictionary.Type>
        {
            val result: MutableList<BiomeDictionary.Type> = mutableListOf()

            for (input in worldGenConfig)
            {
                val configInput = getConfigInput(getSplitInput(input))
                if ("type" inNotNull configInput[0] && configInput.isNotNull(1))
                {
                    result.add(BiomeDictionary.Type.getType(configInput[1]))
                }
            }
            return result
        }

        /**
         * Returns the valid dimension ID for this configuration.
         */
        fun getDimension(): Int
        {
            return if (getSplitInput(indexedInput).isNotNull(0)) getSplitInput(indexedInput)[0].toInt() else 0
        }

        /**
         * 1. Checks if the dimension is valid.
         * 2. Checks if the condition is not null.
         * 3. Gets all BiomeDictionary Types and biomes for this configuration.
         */
        fun canGenerate(biome: Biome, dimension: Int): Boolean
        {
            if (dimension != getDimension()) return false

            val configInput = getConfigInput(getSplitInput(indexedInput))
            if (configInput.isNotNull(0))
            {
                val types = getTypes()
                val biomes = getBiomes()

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

        fun getGenerationChance(): Double
        {
            return if (getSplitInput(indexedInput).isNotNull(2)) getSplitInput(indexedInput)[2].toDouble() else 0.0
        }

        fun getPatchAttempts(): Int
        {
            return if (getSplitInput(indexedInput).isNotNull(3)) getSplitInput(indexedInput)[3].toInt() else 0
        }

        fun getPlantAttempts(): Int
        {
            return if (getSplitInput(indexedInput).isNotNull(4)) getSplitInput(indexedInput)[4].toInt() else 0
        }
    }
}
