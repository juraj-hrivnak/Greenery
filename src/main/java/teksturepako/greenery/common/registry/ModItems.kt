@file:Suppress("MemberVisibilityCanBePrivate")

package teksturepako.greenery.common.registry

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.item.ItemDriedKelp
import teksturepako.greenery.common.item.ItemKelpSoup

object ModItems
{
    val itemKelpSoup = ItemKelpSoup()
    val itemDriedKelp = ItemDriedKelp()

    fun register(registry: IForgeRegistry<Item>)
    {
        registry.register(itemKelpSoup)
        registry.register(itemDriedKelp)
    }

    @SideOnly(Side.CLIENT)
    fun registerModels()
    {
        itemKelpSoup.registerItemModel()
        itemDriedKelp.registerItemModel()
    }

    fun initOreDictionary()
    {
        "plant/submerged/kelp".registerOreDict("cropSeaweed")
    }

    /**
     * Registers an OreDict for an item if it's not null.
     */
    private fun String.registerOreDict(ore: String)
    {
        ForgeRegistries.ITEMS.getValue(ResourceLocation("${Greenery.MODID}:$this"))?.let {
            OreDictionary.registerOre(ore, it)
        }
    }
}