package teksturepako.greenery.common.util

import git.jbredwards.fluidlogged_api.api.fluid.IFluidloggableFluid
import git.jbredwards.fluidlogged_api.api.util.FluidState
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
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

        val fluidState: FluidState = FluidloggedUtils.getFluidState(world as IBlockAccess, pos)

        return if (block is IFluidloggableFluid && (block as IFluidloggableFluid).isFluidloggableFluid(fluidState))
        {
            fluidState.fluid.name in fluids
        }
        else false
    }

    fun canGenerateOnFluids(fluids: List<String>, world: World, pos: BlockPos): Boolean
    {
        val fluidState: FluidState = FluidloggedUtils.getFluidState(world as IBlockAccess, pos)
        if (fluidState.isEmpty) return false
        return areFluidsValid(fluids, fluidState.fluid)
    }
}
