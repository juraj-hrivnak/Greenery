package teksturepako.greenery.common.config.plant.upland

import net.minecraftforge.common.config.Config
import teksturepako.greenery.common.config.plant.upland.tall.*

class Tall
{
    @Config.Name("Barley")
    @Config.Comment("Options for Barley")
    @JvmField
    val barley = Barley()

    @Config.Name("Eagle Fern")
    @Config.Comment("Options for Eagle Fern")
    @JvmField
    val eagleFern = EagleFern()

    @Config.Name("Foxtail")
    @Config.Comment("Options for Foxtail")
    @JvmField
    val foxtail = Foxtail()

    @Config.Name("Nettle")
    @Config.Comment("Options for Nettle")
    @JvmField
    val nettle = Nettle()

    @Config.Name("Ryegrass")
    @Config.Comment("Options for Ryegrass")
    @JvmField
    val ryegrass = Ryegrass()
}