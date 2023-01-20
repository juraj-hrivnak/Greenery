package teksturepako.greenery.common.item

import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery

class ItemDriedKelp : ItemFood(1, 0.6f, false)
{
    companion object
    {
        const val NAME = "dried_kelp"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"
    }

    init
    {
        setRegistryName(NAME)
        translationKey = NAME
        creativeTab = Greenery.creativeTab
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemRenderer(this, 0, registryName.toString())
    }

    override fun getItemBurnTime(itemStack: ItemStack): Int
    {
        return 4000
    }

    override fun getMaxItemUseDuration(stack: ItemStack): Int
    {
        return 16
    }
}