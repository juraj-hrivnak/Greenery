package teksturepako.greenery.common.block.plant

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

interface IGreeneryPlant
{
    /**
     * World generator configs
     */
    val worldGenConfig: MutableList<String>

    /**
     * Function for world generator
     */
    fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
}