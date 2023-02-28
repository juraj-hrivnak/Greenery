package teksturepako.greenery.common.world.gen

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.IWorldGenerator
import teksturepako.greenery.common.block.plant.IGreeneryPlant
import java.util.*

interface IPlantGenerator : IWorldGenerator
{
    /** The block to be generated. */
    val block: IGreeneryPlant

    /** Plant generator */
    fun generatePlants(plantAttempts: Int, world: World, rand: Random, targetPos: BlockPos, flags: Int)
}