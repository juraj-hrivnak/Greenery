package teksturepako.greenery.common.config.plant.submerged

import net.minecraftforge.common.config.Config

class Watermilfoil
{
    @Config.Name("[1] World Gen")
    @Config.Comment(
        "Format:",
        "dimension (Int) | biome:modid:name (ResourceLocation) or type:name (BiomeDictionary.Type) or anywhere | generationChance (Double) | patchAttempts (Int) | plantAttempts (Int)"
    )
    @JvmField
    var worldGen = arrayOf("0 | type:river | 1.0 | 32 | 64")

    @Config.Name("[2] Compatible Fluids")
    @Config.Comment("A list of compatible fluids.")
    @JvmField
    var compatibleFluids = arrayOf("water")
}