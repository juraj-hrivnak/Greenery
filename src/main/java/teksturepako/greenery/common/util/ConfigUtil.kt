package teksturepako.greenery.common.util

import net.minecraftforge.common.BiomeDictionary
import teksturepako.greenery.Greenery

object ConfigUtil
{
    fun parseValidBiomeTypes(types: MutableList<String>): Boolean
    {
        val validTypes = BiomeDictionary.Type.getAll().toList()

        for (input in types)
        {
            val type = BiomeDictionary.Type.getType(input)

            if (type !in validTypes)
            {
                Greenery.logger.warn("   > Typo in \"valid biome dictionary types\" config:    ")
                Greenery.logger.warn("   > \"$type\"                                           ")
                Greenery.logger.warn("   > Valid biome dictionary types are:                   ")
                Greenery.logger.warn("   > $validTypes                                         ")
                Greenery.logger.warn("                                                         ")

                return false
            }
        }
        return true
    }
}