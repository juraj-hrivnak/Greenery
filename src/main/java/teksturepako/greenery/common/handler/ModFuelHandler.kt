@file:Suppress("DEPRECATION")

package teksturepako.greenery.common.handler

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.IFuelHandler
import teksturepako.greenery.common.registry.ModBlocks

class ModFuelHandler : IFuelHandler
{
    override fun getBurnTime(fuel: ItemStack?): Int
    {
        val item = fuel?.item ?: return 0
        if (item == ModBlocks.blockDriedKelp.itemBlock) return 4000
        return 0
    }
}