package teksturepako.greenery.common.util

import git.jbredwards.fluidlogged_api.api.block.IFluidloggableFluid
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid

object FluidUtil {
    fun areFluidsValid(fluids: MutableList<String>, fluid: Fluid): Boolean {
        while (fluid.name in fluids) return true
        return false
    }

    fun canGenerateInFluids(fluids: MutableList<String>, world: World, pos: BlockPos): Boolean {
        val block = world.getBlockState(pos).block
        return block is IFluidloggableFluid && block.fluid.name in fluids
    }
}
