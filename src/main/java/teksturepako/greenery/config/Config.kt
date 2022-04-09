package teksturepako.greenery.config

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.Config.*

object Config {

    val generation: Generation
        get() = _Internal.generation

    class Generation {

        @Name("Arrowhead")
        @Comment("Options for Arrowhead")
        @JvmField
        val arrowhead = Arrowhead()

        @Name("Cattail")
        @Comment("Options for Arrowhead")
        @JvmField
        val cattail = Cattail()

        class Arrowhead {
            @Name("Generation Chance")
            @Comment("The chance to attempt generating in a given chunk.")
            @Config.RangeDouble(min = 0.0, max = 1.0)
            @SlidingOption
            @JvmField
            var generationChance = 0.5

            @Name("Patch Generation Attempts")
            @Comment("Attempts to generate a patch in a given chunk.")
            @Config.RangeInt(min = 0, max = 32)
            @SlidingOption
            @JvmField
            var patchAttempts = 16

            @Name("Plant Generation Attempts")
            @Comment("Attempts to generate a plant in every patch.")
            @Config.RangeInt(min = 0, max = 64)
            @SlidingOption
            @JvmField
            var plantAttempts = 64

            @Name("Valid Biome Dictionary Types")
            @Comment(
                "A list of biome dictionary types in which a plant can generate.",
                "Leave empty to disable checking for biome dictionary types."
            )
            @JvmField
            var validBiomeTypes = arrayOf("RIVER", "WET", "SWAMP", "LUSH")
        }

        class Cattail {
            @Name("Generation Chance")
            @Comment("The chance to attempt generating in a given chunk.")
            @Config.RangeDouble(min = 0.0, max = 1.0)
            @SlidingOption
            @JvmField
            var generationChance = 0.5

            @Name("Patch Generation Attempts")
            @Comment("Attempts to generate a patch in a given chunk.")
            @Config.RangeInt(min = 0, max = 32)
            @SlidingOption
            @JvmField
            var patchAttempts = 16

            @Name("Plant Generation Attempts")
            @Comment("Attempts to generate a plant in every patch.")
            @Config.RangeInt(min = 0, max = 64)
            @SlidingOption
            @JvmField
            var plantAttempts = 64

            @Name("Valid Biome Dictionary Types")
            @Comment(
                "A list of biome dictionary types in which a plant can generate.",
                "Leave empty to disable checking for biome dictionary types."
            )
            @JvmField
            var validBiomeTypes = arrayOf("RIVER", "WET", "SWAMP", "LUSH")
        }

    }

}