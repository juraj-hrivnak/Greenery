package teksturepako.greenery.client

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.registry.ModBlocks

class GreeneryCreativeTab : CreativeTabs(Greenery.MODID)
{
    override fun createIcon(): ItemStack
    {
        return ItemStack(ModBlocks.blockCattail)
    }

    override fun hasSearchBar(): Boolean
    {
        return false
    }
}