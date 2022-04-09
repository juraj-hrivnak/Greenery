package teksturepako.greenery.common.item

import net.minecraft.item.Item
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery

class ItemModIcon : Item() {
    companion object {
        const val NAME = "mod_icon"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"
    }

    init {
        setRegistryName(NAME)
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel() {
        Greenery.proxy.registerItemRenderer(this, 0, registryName.toString())
    }
}