package teksturepako.greenery.common.util

import teksturepako.greenery.Greenery

object ConfigUtil {
    fun parseValidBiomeTypes(types: MutableList<String>) {
        val validTypes = arrayOf(
            "HOT",
            "COLD",
            "SPARSE",
            "DENSE",
            "WET",
            "DRY",
            "SAVANNA",
            "CONIFEROUS",
            "JUNGLE",
            "SPOOKY",
            "DEAD",
            "LUSH",
            "NETHER",
            "END",
            "MUSHROOM",
            "MAGICAL",
            "RARE",
            "OCEAN",
            "RIVER",
            "WATER",
            "MESA",
            "FOREST",
            "PLAINS",
            "MOUNTAIN",
            "HILLS",
            "SWAMP",
            "SANDY",
            "SNOWY",
            "WASTELAND",
            "BEACH",
            "VOID"
        )

        for (type in types) {
            if (type !in validTypes) {
                Greenery.logger.warn("   ├── Typo in \"valid biome dictionary types\" config:")
                Greenery.logger.warn("   └── \"$type\"")
            }
        }
    }
}