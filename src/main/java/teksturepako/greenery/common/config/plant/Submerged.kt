package teksturepako.greenery.common.config.plant

import net.minecraftforge.common.config.Config
import teksturepako.greenery.common.config.plant.submerged.Kelp
import teksturepako.greenery.common.config.plant.submerged.Seagrass
import teksturepako.greenery.common.config.plant.submerged.Watermilfoil

class Submerged
{
    @Config.Name("Kelp")
    @Config.Comment("Options for Kelp")
    @JvmField
    val kelp = Kelp()

    @Config.Name("Seagrass")
    @Config.Comment("Options for Seagrass")
    @JvmField
    val seagrass = Seagrass()

    @Config.Name("Watermilfoil")
    @Config.Comment("Options for Watermilfoil")
    @JvmField
    val watermilfoil = Watermilfoil()
}