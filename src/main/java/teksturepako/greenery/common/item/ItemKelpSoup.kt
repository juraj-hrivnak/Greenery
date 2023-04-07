package teksturepako.greenery.common.item

import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery

class ItemKelpSoup : ItemFood(6, 0.6f, false)
{
    companion object
    {
        const val NAME = "kelp_soup"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"
    }

    init
    {
        setRegistryName(NAME)
        translationKey = "${Greenery.MODID}.$NAME"
        creativeTab = Greenery.creativeTab
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemRenderer(this, 0, registryName.toString())
    }

    override fun onItemUseFinish(stack: ItemStack, worldIn: World, entityLiving: EntityLivingBase): ItemStack
    {
        super.onItemUseFinish(stack, worldIn, entityLiving)
        return ItemStack(Items.BOWL)
    }
}