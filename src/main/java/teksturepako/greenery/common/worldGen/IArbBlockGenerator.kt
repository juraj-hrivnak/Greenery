package teksturepako.greenery.common.worldGen

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.IWorldGenerator
import java.util.*

interface IArbBlockGenerator : IWorldGenerator
{
    val name: String

    val blockStates: List<IBlockState>

    val worldGen: List<String>
    val soilFunc: (IBlockState) -> Boolean

    fun generateBlocks(plantAttempts: Int, world: World, rand: Random, targetPos: BlockPos, flags: Int)
}
