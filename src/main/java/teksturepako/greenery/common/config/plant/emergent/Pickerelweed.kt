package teksturepako.greenery.common.config.plant.emergent

import net.minecraftforge.common.config.Config

class Pickerelweed
{
    @Config.Name("[1] World Gen")
    @Config.Comment(
        "Format:",
        "dimension (Int) | biome:modid:name (ResourceLocation) or type:name (BiomeDictionary.Type) or anywhere | generationChance (Double) | patchAttempts (Int) | plantAttempts (Int)"
    )
    @JvmField
    var worldGen = arrayOf(
        "0 | type:river | 0.5 | 16 | 64",
        "0 | type:wet | 0.5 | 16 | 64",
        "0 | type:swamp | 0.5 | 16 | 64",
        "0 | type:lush | 0.5 | 16 | 64"
    )
}