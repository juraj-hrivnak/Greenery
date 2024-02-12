package teksturepako.greenery.common.util

import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.fml.common.registry.ForgeRegistries
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.world.WorldGenParser
import teksturepako.greenery.common.world.gen.IPlantGenerator
import java.util.*

object ConfigUtil
{
    private val validTypes: MutableList<BiomeDictionary.Type> = ArrayList()

    /**
     * IPlantGenerator Parser
     * @return true when errored
     */
    fun parseGenerators(generators: MutableList<IPlantGenerator>, printParsing: Boolean): Boolean
    {
        if (printParsing)
        {
            Greenery.logger.info("Loading world generators:")
        }

        if (generators.isEmpty())
        {
            if (printParsing)
            {
                Greenery.logger.error("No generators found!")
            }
            return true
        }

        var errored = false

        for (generator in generators)
        {
            var types: List<BiomeDictionary.Type> = emptyList()
            var biomes: List<ResourceLocation> = emptyList()

            for (input in generator.block.worldGen)
            {
                val config = WorldGenParser(input, generator.block.worldGen)
                types = config.types
                biomes = config.biomesResLocs

                errored = parseBiomeDictionaries(types, false) || parseBiomes(biomes, false)
            }

            if (printParsing)
            {
                if (errored)
                {
                    Greenery.logger.warn("  ! ${generator.block.localizedName}")
                    Greenery.logger.error("    > ${generator.block.worldGen}")
                    parseBiomeDictionaries(types, true)
                    parseBiomes(biomes, true)
                }
                else
                {
                    Greenery.logger.info("  > ${generator.block.localizedName}")
                    Greenery.logger.info("     > ${generator.block.worldGen}")
                }
            }
        }

        if (printParsing && errored) printValidBiomeDictionaries()

        return errored
    }

    /**
     * Biome Dictionary Config Parser
     * @return true when errored
     */
    private fun parseBiomeDictionaries(types: List<BiomeDictionary.Type>, printErrors: Boolean): Boolean
    {
        for (type in types)
        {
            if (type !in getValidBiomeDictionaryTypes())
            {
                if (printErrors)
                {
                    Greenery.logger.error("    > Invalid biome dictionary type: \"${type.name.lowercase(Locale.getDefault())}\"")
                }
                return true
            }
        }
        return false
    }

    /**
     * Prints all valid BiomeDictionary types.
     */
    private fun printValidBiomeDictionaries()
    {
        Greenery.logger.warn("Valid biome dictionary types are:")
        Greenery.logger.warn("${getValidBiomeDictionaryTypes()}")
    }

    /**
     * Gets all valid biome dictionary types before new ones are added.
     */
    private fun getValidBiomeDictionaryTypes(): List<BiomeDictionary.Type>
    {
        val allTypes = BiomeDictionary.Type.getAll().toList()
        if (validTypes.isEmpty())
        {
            validTypes.addAll(allTypes)
        }
        return validTypes
    }

    /**
     * Biome Config Parser
     * @return true when errored
     */
    private fun parseBiomes(biomes: List<ResourceLocation>, printErrors: Boolean): Boolean
    {
        for (biome in biomes)
        {
            if (!ForgeRegistries.BIOMES.containsKey(biome))
            {
                if (printErrors)
                {
                    Greenery.logger.error("    > Invalid biome: \"${biome}\"")
                }
                return true
            }
        }
        return false
    }
}