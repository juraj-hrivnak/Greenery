package teksturepako.greenery.common.config

import net.minecraftforge.common.config.Config.*
import teksturepako.greenery.common.config.plant.Submerged

object Config
{
    val global: GlobalSettings = _Internal.GLOBAL_SETTINGS
    val plant: PlantSettings = _Internal.PLANT_SETTINGS

    class GlobalSettings
    {
        @Name("[1] Remove Grass")
        @Comment("Whether to remove the vanilla and Biomes O' Plenty grass or not.")
        @JvmField
        var removeGrass = true

        @Name("[2] Generate Plants From Bonemeal")
        @Comment("Whether to grow new plants when using bonemeal or not.")
        @JvmField
        var genPlantsFromBonemeal = true

        @Name("[3] Slowdown Modifier")
        @Comment("Slows down the movement through plants. Higher = slower.")
        @SlidingOption
        @JvmField
        var slowdownModifier = 0.1F
    }

    class PlantSettings
    {
        @Name("Submerged")
        @Comment("Options for Submerged plants.")
        @JvmField
        val submerged = Submerged()
    }
}