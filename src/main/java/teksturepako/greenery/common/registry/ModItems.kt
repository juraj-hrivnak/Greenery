package teksturepako.greenery.common.registry

import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.common.block.plant.emergent.ItemBlockArrowhead
import teksturepako.greenery.common.block.plant.emergent.ItemBlockCattail
import teksturepako.greenery.common.item.ItemDriedKelp
import teksturepako.greenery.common.item.ItemKelpSoup
import teksturepako.greenery.common.registry.ModBlocks.blockGrass
import teksturepako.greenery.common.registry.ModBlocks.blockRyegrass
import teksturepako.greenery.common.registry.ModBlocks.blockEagleFern
import teksturepako.greenery.common.registry.ModBlocks.blockFoxtail

object ModItems {

    val itemKelpSoup = ItemKelpSoup()
    val itemDriedKelp = ItemDriedKelp()
    val itemBlockCattail = ItemBlockCattail()
    val itemBlockArrowhead = ItemBlockArrowhead()

    fun register(registry: IForgeRegistry<Item>) {
        registry.register(itemKelpSoup)
        registry.register(itemDriedKelp)
        registry.register(itemBlockCattail)
        registry.register(itemBlockArrowhead)
    }

    @SideOnly(Side.CLIENT)
    fun registerModels() {
        itemKelpSoup.registerItemModel()
        itemDriedKelp.registerItemModel()
        itemBlockCattail.registerItemModel()
        itemBlockArrowhead.registerItemModel()
    }

    @SideOnly(Side.CLIENT)
    fun registerItemColorHandlers(event: ColorHandlerEvent.Item) {
        itemBlockArrowhead.registerItemColorHandler(event)
        blockFoxtail.registerItemColorHandler(event)
        blockEagleFern.registerItemColorHandler(event)
        blockRyegrass.registerItemColorHandler(event)
        blockGrass.registerItemColorHandler(event)
    }

    fun initOreDictionary() {
        OreDictionary.registerOre("cropSeaweed", ModBlocks.blockKelp)
    }
}