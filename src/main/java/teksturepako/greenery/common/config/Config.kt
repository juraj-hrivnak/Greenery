package teksturepako.greenery.common.config

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

        @Name("Grass")
        @Comment("Options for Grass")
        @JvmField
        val grass = Grass()

        @Name("Fern")
        @Comment("Options for Fern")
        @JvmField
        val fern = Fern()

        @Name("Kelp")
        @Comment("Options for Kelp")
        @JvmField
        val kelp = Kelp()

        @Name("Rivergrass")
        @Comment("Options for Rivergrass")
        @JvmField
        val rivergrass = Rivergrass()

        @Name("Seagrass")
        @Comment("Options for Seagrass")
        @JvmField
        val seagrass = Seagrass()

        @Name("Global Grass Generation Multiplier")
        @Comment("Multiplies the patch attempts of grass by given number.")
        @Config.RangeInt(min = 1, max = 4)
        @SlidingOption
        @JvmField
        var generationMultiplier = 1


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

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
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

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
        }

        class Grass {
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
            var validBiomeTypes = emptyArray<String>()

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
        }

        class Fern {
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
            var validBiomeTypes = emptyArray<String>()

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
        }

        class Kelp {
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
            var validBiomeTypes = arrayOf("OCEAN", "BEACH")

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
        }

        class Rivergrass {
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
            var validBiomeTypes = arrayOf("RIVER")

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
        }

        class Seagrass {
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
            var validBiomeTypes = arrayOf("OCEAN", "BEACH")

            @Name("Valid Biome Dictionary Types Inverted")
            @Comment("Whether Valid Biome Dictionary Types are inverted.")
            @JvmField
            var inverted = false
        }


    }

}