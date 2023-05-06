package teksturepako.greenery.common.config.json

import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.json.Deserializer.getOrCreateSubfolder

object PlantDefaults {

    val plantDir = Greenery.configFolder.getOrCreateSubfolder("plants")
    val emergentDir = plantDir.getOrCreateSubfolder("emergent")
    val submergedDir = plantDir.getOrCreateSubfolder("submerged")
    val submergedKelpLikeDir = submergedDir.getOrCreateSubfolder("kelp_like")
    val submergedTallDir = submergedDir.getOrCreateSubfolder("tall")
    val uplandDir = plantDir.getOrCreateSubfolder("upland")
    val uplandTallDir = uplandDir.getOrCreateSubfolder("tall")

    val emergentPlants = listOf(
        PlantData(
            name = "cattail",
            worldGen = mutableListOf(
                "0 | type:river | 0.5 | 16 | 64",
                "0 | type:wet | 0.5 | 16 | 64",
                "0 | type:swamp | 0.5 | 16 | 64",
                "0 | type:lush | 0.5 | 16 | 64"
            ),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "arrowhead",
            worldGen = mutableListOf(
                "0 | type:river | 0.5 | 16 | 64",
                "0 | type:wet | 0.5 | 16 | 64",
                "0 | type:swamp | 0.5 | 16 | 64",
                "0 | type:lush | 0.5 | 16 | 64"
            ),
            compatibleFluids = mutableListOf("water"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "pickerelweed",
            worldGen = mutableListOf(
                "0 | type:river | 0.5 | 16 | 64",
                "0 | type:wet | 0.5 | 16 | 64",
                "0 | type:swamp | 0.5 | 16 | 64",
                "0 | type:lush | 0.5 | 16 | 64"
            ),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        )
    )

    val submergedKelpLikePlants = listOf(
        PlantData(
            name = "kelp",
            worldGen = mutableListOf(
                "0 | type:ocean | 0.5 | 14 | 64",
                "0 | type:beach | 0.5 | 14 | 64"
            ),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        )
    )

    val submergedTallPlants = listOf(
        PlantData(
            name = "seagrass",
            worldGen = mutableListOf(
                "0 | type:ocean | 1.0 | 24 | 64",
                "0 | type:beach | 1.0 | 24 | 64"
            ),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "watermilfoil",
            worldGen = mutableListOf(
                "0 | type:river | 1.0 | 32 | 64"
            ),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        )
    )

    val uplandTallPlants = listOf(
        PlantData(
            name = "foxtail",
            worldGen = mutableListOf(
                "0 | !type:savanna | 1.0 | 16 | 64",
                "0 | !type:plains | 1.0 | 16 | 64",
                "0 | !type:beach | 1.0 | 16 | 64",
                "0 | !type:dry | 1.0 | 16 | 64",
                "0 | !type:jungle | 1.0 | 16 | 64"
            ),
            drops = mutableListOf("\$defaultSeeds | 0.2"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "eagle_fern",
            worldGen = mutableListOf("0 | anywhere | 1.0 | 16 | 32"),
            drops = mutableListOf("\$defaultSeeds | 0.2"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "ryegrass",
            worldGen = mutableListOf(
                "0 | type:savanna | 1.0 | 32 | 64",
                "0 | type:plains | 1.0 | 32 | 64",
                "0 | type:beach | 1.0 | 32 | 64",
                "0 | type:dry | 1.0 | 32 | 64",
                "0 | type:jungle | 1.0 | 32 | 64"
            ),
            drops = mutableListOf("\$defaultSeeds | 0.2"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "nettle",
            worldGen = mutableListOf("0 | anywhere | 0.5 | 8 | 32"),
            isSolid = false,
            isHarmful = true
        ),
        PlantData(
            name = "barley",
            worldGen = mutableListOf(
                "0 | type:dry | 0.5 | 1 | 8",
                "0 | type:sparse | 0.5 | 1 | 8",
                "0 | biome:biomesoplenty:pasture | 1.0 | 128 | 64"
            ),
            isSolid = false,
            isHarmful = false
        )
    )

}