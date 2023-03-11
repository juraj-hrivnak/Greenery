package teksturepako.greenery.common.registry

import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.common.item.ItemDriedKelp
import teksturepako.greenery.common.item.ItemKelpSoup
import teksturepako.greenery.common.registry.ModBlocks.blockArrowhead
import teksturepako.greenery.common.registry.ModBlocks.blockEagleFern
import teksturepako.greenery.common.registry.ModBlocks.blockFoxtail
import teksturepako.greenery.common.registry.ModBlocks.blockGrass
import teksturepako.greenery.common.registry.ModBlocks.blockRyegrass

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

    @SideOnly(Side.CLIENT)
    fun registerItemColorHandlers(event: ColorHandlerEvent.Item)
    {
        blockArrowhead.registerItemColorHandler(event)
        blockFoxtail.registerItemColorHandler(event)
        blockEagleFern.registerItemColorHandler(event)
        blockRyegrass.registerItemColorHandler(event)
        blockGrass.registerItemColorHandler(event)
    }

    fun initOreDictionary()
    {
        OreDictionary.registerOre("cropSeaweed", ModBlocks.blockKelp)
    }
}