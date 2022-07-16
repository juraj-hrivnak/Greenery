package teksturepako.greenery.common.util

import net.minecraftforge.fluids.Fluid

object FluidUtil {
    fun isFluidValid(fluids: MutableList<String>, fluid: Fluid): Boolean {
        while (fluid.name in fluids) return true
        return false
    }
}