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
        var extendedBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome)
        if (extendedBiome == null)
        {
            extendedBiome = ExtendedBiomeWrapper(biome)
            BOPBiomes.REG_INSTANCE.registerBiome(extendedBiome, extendedBiome.getBaseBiome().biomeName.toLowerCase())
        }
        return extendedBiome
    }

    fun getBiomeInChunk(world: World, chunkX: Int, chunkZ: Int): Biome
    {
        return world.getBiomeForCoordsBody(BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8))
    }

    data class Parser(private val indexedInput: String, private val worldGenConfig: MutableList<String>)
    {
        /**
         * Filter & split input when "|"
         */
        private fun getSplitInput(string: String): List<String>
        {
            return if (string.isNotEmpty()) string.filter { !it.isWhitespace() }.trim().split("|") else emptyList()
        }

        /**
         * Split input when ":"
         */
        private fun getConfigInput(splitInput: List<String>): List<String>
        {
            return if (splitInput.isNotNull(1)) splitInput[1].split(":") else emptyList()
        }

        fun getBiomesResLoc(): List<ResourceLocation>
        {
            val result: MutableList<ResourceLocation> = emptyList<ResourceLocation>().toMutableList()

            for (input in worldGenConfig)
            {
                val configInput = getConfigInput(getSplitInput(input))
                if (configInput.isNotNull(0) && "biome" in configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
                {
                    result.add(ResourceLocation(configInput[1], configInput[2]))
                }
            }
            return result
        }

        fun getBiomes(): List<Biome>
        {
            val result: MutableList<Biome> = emptyList<Biome>().toMutableList()

            for (input in worldGenConfig)
            {
                val configInput = getConfigInput(getSplitInput(input))
                if (configInput.isNotNull(0) && "biome" in configInput[0] && configInput.isNotNull(1) && configInput.isNotNull(2))
                {
                    ForgeRegistries.BIOMES.getValue(ResourceLocation(configInput[1], configInput[2]))?.let { result.add(it) }
                }
            }
            return result
        }

        fun getTypes(): List<BiomeDictionary.Type>
        {
            val result: MutableList<BiomeDictionary.Type> = emptyList<BiomeDictionary.Type>().toMutableList()

            for (input in worldGenConfig)
            {
                val configInput = getConfigInput(getSplitInput(input))
                if (configInput.isNotNull(0) && "type" in configInput[0] && configInput.isNotNull(1))
                {
                    result.add(BiomeDictionary.Type.getType(configInput[1]))
                }
            }
            return result
        }

        fun getDimension(): Int
        {
            return if (getSplitInput(indexedInput).isNotNull(0)) getSplitInput(indexedInput)[0].toInt() else 0
        }

        fun canGenerate(biomeInput: Biome, dimensionInput: Int): Boolean
        {
            if (dimensionInput != getDimension()) return false

            val configInput = getConfigInput(getSplitInput(indexedInput))
            if (configInput.isNotNull(0))
            {
                when
                {
                    "!" in configInput[0] -> if ("type" in configInput[0])
                    {
                        return getTypes().none { BiomeDictionary.hasType(biomeInput, it) }
                    }
                    else if ("biome" in configInput[0])
                    {
                        return getBiomes().none { biomeInput.registryName == it.registryName }
                    }
                    "type" in configInput[0] -> return getTypes().any { BiomeDictionary.hasType(biomeInput, it) }
                    "biome" in configInput[0] -> return getBiomes().any { biomeInput.registryName == it.registryName }
                    "anywhere" in configInput[0] -> return true
                }
            }
            return false
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

    private fun <T> List<T>.isNotNull(index: Int): Boolean
    {
        return if (index in 0..lastIndex) get(index) != null else false
    }
}
