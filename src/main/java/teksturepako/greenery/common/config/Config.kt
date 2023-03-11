package teksturepako.greenery.common.config

import net.minecraftforge.common.config.Config.*
import teksturepako.greenery.common.config.plant.Emergent
import teksturepako.greenery.common.config.plant.Floating
import teksturepako.greenery.common.config.plant.Submerged
import teksturepako.greenery.common.config.plant.Upland

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
        @Name("Emergent")
        @Comment("Options for Emergent plants.")
        @JvmField
        val emergent = Emergent()

        @Name("Floating")
        @Comment("Options for Floating plants.")
        @JvmField
        val floating = Floating()

        @Name("Submerged")
        @Comment("Options for Submerged plants.")
        @JvmField
        val submerged = Submerged()

        @Name("Upland")
        @Comment("Options for Upland plants.")
        @JvmField
        val upland = Upland()
    }
}