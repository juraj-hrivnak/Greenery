package teksturepako.greenery.common.util

import net.minecraftforge.common.BiomeDictionary
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.world.gen.IPlantGenerator

object ConfigUtil
{
    private val validTypes: MutableList<BiomeDictionary.Type> = ArrayList()

    /**
     * IPlantGenerator Parser
     * @return true when errored
     */
    fun parseGenerators(generators: MutableList<IPlantGenerator>, print: Boolean): Boolean
    {
        if (print)
        {
            Greenery.logger.info("------------------------------------------------------     ")
            Greenery.logger.info("Loading world generators:")
        }

        if (generators.isEmpty())
        {
            if (print)
            {
                Greenery.logger.info("No generators found!")
                Greenery.logger.error("------------------------------------------------------     ")
            }
            return true
        }

        var printBiomeTypes = false
        val errored = false

        for (generator in generators)
        {
            if (print)
            {
                if (errored)
                {
                    Greenery.logger.warn("    ! \"${generator.block.javaClass.name.replace("teksturepako.greenery.common.block.", "")}\"")
                }
                else
                {
                    Greenery.logger.info("  > \"${generator.block.javaClass.name.replace("teksturepako.greenery.common.block.", "")}\"")
                }
                Greenery.logger.info("     > ${generator.block.worldGenConfig}")
            }

            if (!printBiomeTypes)
            {
                printBiomeTypes = errored
            }
        }

        if (printBiomeTypes && print)
        {
            printValidBiomeDictionaries()
        }

        if (print) Greenery.logger.info("------------------------------------------------------     ")

        return errored
    }

    /**
     * Biome Dictionary Config Parser
     * @return Boolean (true when errored)
     */
    private fun parseBiomeDictionaries(types: MutableList<String>, printErrors: Boolean): Boolean
    {
        for (input in types)
        {
            val type = BiomeDictionary.Type.getType(input)
            if (type !in getValidBiomeDictionaryTypes())
            {
                if (printErrors)
                {
                    Greenery.logger.error("     > Typo in \"valid biome dictionary types\" config:   ")
                    Greenery.logger.error("     > \"$type\"                                          ")
                    Greenery.logger.error("     ------------------------------------------------     ")
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
        Greenery.logger.warn("Valid biome dictionary types are:   ")
        Greenery.logger.warn("${getValidBiomeDictionaryTypes()}   ")
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
}