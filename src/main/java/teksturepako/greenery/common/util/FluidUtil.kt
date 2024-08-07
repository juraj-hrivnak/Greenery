package teksturepako.greenery.common.util

import git.jbredwards.fluidlogged_api.api.block.IFluidloggableFluid
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid

object FluidUtil
{
    fun areFluidsValid(fluids: List<String>, fluid: Fluid): Boolean
    {
        return fluid.name in fluids
    }

    fun canGenerateInFluids(fluids: List<String>, world: World, pos: BlockPos): Boolean
    {
        val block = world.getBlockState(pos).block
        return if (block is IFluidloggableFluid && (block as IFluidloggableFluid).isFluidloggableFluid)
        {
            block.fluid.name in fluids
        }
        else false
    }
}
