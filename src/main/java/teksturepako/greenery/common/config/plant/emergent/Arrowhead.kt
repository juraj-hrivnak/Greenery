package teksturepako.greenery.common.config.plant.emergent

import net.minecraftforge.common.config.Config

class Arrowhead
{
    @Config.Name("Generation Chance")
    @Config.Comment("The chance to attempt generating in a given chunk.")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    @Config.SlidingOption
    @JvmField
    var generationChance = 0.5

    @Config.Name("Patch Generation Attempts")
    @Config.Comment("Attempts to generate a patch in a given chunk.")
    @Config.RangeInt(min = 0, max = 32)
    @Config.SlidingOption
    @JvmField
    var patchAttempts = 16

    @Config.Name("Plant Generation Attempts")
    @Config.Comment("Attempts to generate a plant in every patch.")
    @Config.RangeInt(min = 0, max = 64)
    @Config.SlidingOption
    @JvmField
    var plantAttempts = 64

    @Config.Name("Valid Biome Dictionary Types")
    @Config.Comment(
        "A list of biome dictionary types in which a plant can generate.",
        "Leave empty to disable checking for biome dictionary types."
    )
    @JvmField
    var validBiomeTypes = arrayOf("RIVER", "WET", "SWAMP", "LUSH")

    @Config.Name("Valid Biome Dictionary Types Inverted")
    @Config.Comment("Whether Valid Biome Dictionary Types are inverted.")
    @JvmField
    var inverted = false
}