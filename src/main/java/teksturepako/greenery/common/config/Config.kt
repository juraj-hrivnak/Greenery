package teksturepako.greenery.common.config

import net.minecraftforge.common.config.Config
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
        @Name("Global Grass Generation Multiplier")
        @Comment("Multiplies the patch attempts of grass by given number.")
        @Config.RangeInt(min = 1, max = 4)
        @SlidingOption
        @JvmField
        var generationMultiplier = 1

        @Name("Remove Grass")
        @Comment("Removes the vanilla and Biomes O' Plenty grass.")
        @JvmField
        var removeGrass = true

        @Name("Generate plants from bonemeal")
        @Comment("Whether to grow new plants when using bonemeal or not.")
        @JvmField
        var genPlantsFromBonemeal = true

        @Name("Slowdown Modifier")
        @Comment("Slows entity movement through plants. Higher = slower.")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        @SlidingOption
        @JvmField
        var slowdownModifier = 0.1
    }

    class PlantSettings
    {
        @Name("Emergent")
        @Comment("Options for Emergent plants")
        @JvmField
        val emergent = Emergent()

        @Name("Floating")
        @Comment("Options for Floating plants")
        @JvmField
        val floating = Floating()

        @Name("Submerged")
        @Comment("Options for Submerged plants")
        @JvmField
        val submerged = Submerged()

        @Name("Upland")
        @Comment("Options for Upland plants")
        @JvmField
        val upland = Upland()
    }
}