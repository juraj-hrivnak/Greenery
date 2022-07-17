package teksturepako.greenery.common.util

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry

object FluidUtil {
    fun isFluidValid(fluids: MutableList<String>, fluid: Fluid): Boolean {
        if (fluids.isNotEmpty()) {
            while (fluid.name in fluids) return true
            return false
        } else {
            return FluidloggedUtils.isCompatibleFluid(FluidRegistry.WATER, fluid)
        }
    }
}