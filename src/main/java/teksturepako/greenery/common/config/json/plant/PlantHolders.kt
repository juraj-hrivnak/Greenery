@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.config.json.plant

import kotlinx.serialization.encodeToString
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.config.json._json
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.writeText

interface PlantDataHolder
{
    val path: Path
    val defaults: List<PlantData>

    fun encodeDefaults()
    {
        if (!Config.global.genDefaults) return

        for (plantData in defaults)
        {
            val plantDataFile = Path(path.pathString, "${plantData.name}.json")
            if (plantDataFile.exists()) continue
            plantDataFile.writeText(_json.encodeToString(plantData))
        }
    }
}

private val plantsPath = Path(Greenery.configFolder.pathString, "plants").pathString

object Emergent : PlantDataHolder
{
    override val path: Path = Path(plantsPath, "emergent")
    override val defaults = listOf(
        PlantData(
            name = "cattail",
            maxAge = 3,
            worldGen = mutableListOf(
                "0 | type:river | 0.5 | 16 | 64",
                "0 | type:wet | 0.5 | 16 | 64",
                "0 | type:swamp | 0.5 | 16 | 64",
                "0 | type:lush | 0.5 | 16 | 64"
            ),
            allowedSoils = listOf("ground", "sand", "grass", "clay", "rock"),
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
            allowedSoils = listOf("ground", "sand", "grass", "clay", "rock"),
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
            allowedSoils = listOf("ground", "sand", "grass", "clay", "rock"),
            compatibleFluids = mutableListOf("water"),
            isSolid = false,
            isHarmful = false
        )
    )
}

object Floating : PlantDataHolder
{
    override val path = Path(plantsPath, "floating")
    override val defaults = listOf<PlantData>()
}

object Submerged
{
    val path = Path(plantsPath, "submerged")

    object KelpLike : PlantDataHolder
    {
        override val path = Path(Submerged.path.pathString, "kelp_like")
        override val defaults = listOf(
            PlantData(
                name = "kelp",
                maxAge = 15,
                worldGen = mutableListOf(
                    "0 | type:ocean | 0.5 | 14 | 64",
                    "0 | type:beach | 0.5 | 14 | 64"
                ),
                drops = mutableListOf("this | 1.0"),
                allowedSoils = listOf("ground", "sand", "grass", "clay", "rock"),
                compatibleFluids = mutableListOf("water"),
                hasOffset = false,
                isSolid = false,
                isHarmful = false
            )
        )
    }

    object Tall : PlantDataHolder
    {
        override val path = Path(Submerged.path.pathString, "tall")
        override val defaults = listOf(
            PlantData(
                name = "seagrass",
                maxAge = 1,
                worldGen = mutableListOf(
                    "0 | type:ocean | 1.0 | 24 | 64",
                    "0 | type:beach | 1.0 | 24 | 64"
                ),
                drops = mutableListOf("this | 1.0"),
                allowedSoils = listOf("ground", "sand", "grass", "clay", "rock"),
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
                allowedSoils = listOf("ground", "sand", "grass", "clay", "rock"),
                compatibleFluids = mutableListOf("water"),
                isSolid = false,
                isHarmful = false
            )
        )
    }
}

object Upland
{
    val path = Path(plantsPath, "upland")

    object Single : PlantDataHolder
    {
        override val path = Path(Upland.path.pathString, "single")
        override val defaults = listOf<PlantData>()
    }

    object Tall : PlantDataHolder
    {
        override val path = Path(Upland.path.pathString, "tall")
        override val defaults = listOf(
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
                allowedSoils = listOf("grass"),
                hasTintIndex = true,
                isSolid = false,
                isHarmful = false
            ),
            PlantData(
                name = "eagle_fern",
                maxAge = 3,
                worldGen = mutableListOf("0 | anywhere | 1.0 | 16 | 32"),
                drops = mutableListOf("seeds | 0.2"),
                allowedSoils = listOf("grass"),
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
                allowedSoils = listOf("grass"),
                hasTintIndex = true,
                isSolid = false,
                isHarmful = false
            ),
            PlantData(
                name = "nettle",
                maxAge = 3,
                worldGen = mutableListOf("0 | anywhere | 0.5 | 8 | 32"),
                allowedSoils = listOf("grass"),
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
                allowedSoils = listOf("grass"),
                isSolid = false,
                isHarmful = false
            )
        )
    }
}