@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.config.json

import teksturepako.greenery.Greenery
import teksturepako.greenery.common.util.FileUtils.div

object PlantDefaults
{

    val plantDir = Greenery.configFolder / "plants"

    val emergentDir = plantDir / "emergent"

    val floatingDir = plantDir / "floating"

    val submergedDir = plantDir / "submerged"
    val submergedKelpLikeDir = submergedDir / "kelp_like"
    val submergedTallDir = submergedDir / "tall"

    val uplandDir = plantDir / "upland"
    val uplandSingleDir = uplandDir / "single"
    val uplandTallDir = uplandDir / "tall"

    val emergentPlants = listOf(
        PlantData(
            name = "cattail",
            maxAge = 3,
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
            maxAge = 3,
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
            maxAge = 3,
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
            maxAge = 15,
            worldGen = mutableListOf(
                "0 | type:ocean | 0.5 | 14 | 64",
                "0 | type:beach | 0.5 | 14 | 64"
            ),
            drops = mutableListOf("this | 1.0"),
            compatibleFluids = mutableListOf("water"),
            hasOffset = false,
            isSolid = false,
            isHarmful = false
        )
    )

    val submergedTallPlants = listOf(
        PlantData(
            name = "seagrass",
            maxAge = 1,
            worldGen = mutableListOf(
                "0 | type:ocean | 1.0 | 24 | 64",
                "0 | type:beach | 1.0 | 24 | 64"
            ),
            drops = mutableListOf("this | 1.0"),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "watermilfoil",
            maxAge = 1,
            worldGen = mutableListOf(
                "0 | type:river | 1.0 | 32 | 64"
            ),
            drops = mutableListOf("this | 1.0"),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        )
    )

    val uplandTallPlants = listOf(
        PlantData(
            name = "foxtail",
            maxAge = 3,
            worldGen = mutableListOf(
                "0 | !type:savanna | 1.0 | 16 | 64",
                "0 | !type:plains | 1.0 | 16 | 64",
                "0 | !type:beach | 1.0 | 16 | 64",
                "0 | !type:dry | 1.0 | 16 | 64",
                "0 | !type:jungle | 1.0 | 16 | 64"
            ),
            drops = mutableListOf("seeds | 0.2"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "eagle_fern",
            maxAge = 3,
            worldGen = mutableListOf("0 | anywhere | 1.0 | 16 | 32"),
            drops = mutableListOf("seeds | 0.2"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "ryegrass",
            maxAge = 3,
            worldGen = mutableListOf(
                "0 | type:savanna | 1.0 | 32 | 64",
                "0 | type:plains | 1.0 | 32 | 64",
                "0 | type:beach | 1.0 | 32 | 64",
                "0 | type:dry | 1.0 | 32 | 64",
                "0 | type:jungle | 1.0 | 32 | 64"
            ),
            drops = mutableListOf("seeds | 0.2"),
            hasTintIndex = true,
            isSolid = false,
            isHarmful = false
        ),
        PlantData(
            name = "nettle",
            maxAge = 3,
            worldGen = mutableListOf("0 | anywhere | 0.5 | 8 | 32"),
            isSolid = false,
            isHarmful = true
        ),
        PlantData(
            name = "barley",
            maxAge = 3,
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