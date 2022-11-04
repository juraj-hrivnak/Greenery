package teksturepako.greenery.common.config.plant

import net.minecraftforge.common.config.Config
import teksturepako.greenery.common.config.plant.upland.Single
import teksturepako.greenery.common.config.plant.upland.Tall

class Upland
{
    @Config.Name("Single")
    @Config.Comment("Options for Single Upland plants")
    @JvmField
    val single = Single()

    @Config.Name("Tall")
    @Config.Comment("Options for Tall Upland plants")
    @JvmField
    val tall = Tall()
}