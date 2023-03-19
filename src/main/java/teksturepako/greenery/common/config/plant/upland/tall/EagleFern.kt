package teksturepako.greenery.common.config.plant.upland.tall

import net.minecraftforge.common.config.Config

class EagleFern
{
    @Config.Name("[1] World Gen")
    @Config.Comment(
        "Format:",
        "dimension (Int) | biome:modid:name (ResourceLocation) or type:name (BiomeDictionary.Type) or anywhere | generationChance (Double) | patchAttempts (Int) | plantAttempts (Int)"
    )
    @JvmField
    var worldGen = arrayOf("0 | anywhere | 1.0 | 16 | 32")

    @Config.Name("[2] Drops")
    @Config.Comment(
        "A list of items to drop when broken. Format:",
        "(itemStack) mod_name:item_name:count | (chance) 0.0 - 1.0 | (blockState) key=value,key=value"
    )
    @JvmField
    var drops = arrayOf("\$defaultSeeds | 0.2")
}