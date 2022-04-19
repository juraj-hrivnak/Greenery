package teksturepako.greenery.client

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.registry.ModItems

class ModCreativeTab : CreativeTabs(Greenery.MODID) {
    override fun createIcon(): ItemStack {
        return ItemStack(ModItems.itemBlockCattail)
    }

    override fun hasSearchBar(): Boolean {
        return false
    }
}