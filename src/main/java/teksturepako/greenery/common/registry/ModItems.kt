package teksturepako.greenery.common.registry

import net.minecraft.item.Item
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistry
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.block.crop.ItemBlockArrowhead
import teksturepako.greenery.common.block.crop.ItemBlockCattail
import teksturepako.greenery.common.item.ItemDriedKelp
import teksturepako.greenery.common.item.ItemKelpSoup
import teksturepako.greenery.common.item.ItemModIcon

object ModItems {

    val itemModIcon = ItemModIcon()
    val itemKelpSoup = ItemKelpSoup()
    val itemDriedKelp = ItemDriedKelp()
    val itemBlockCattail = ItemBlockCattail()
    val itemBlockArrowhead = ItemBlockArrowhead()

    fun register(registry: IForgeRegistry<Item>) {
        registry.register(itemModIcon)

        if (ModConfig.Kelp.enabled) {
            if (ModConfig.Kelp.kelpSoupEnabled) registry.register(itemKelpSoup)
            if (ModConfig.Kelp.driedKelpEnabled) registry.register(itemDriedKelp)
        }

        registry.register(itemBlockCattail)
        registry.register(itemBlockArrowhead)
    }

    @SideOnly(Side.CLIENT)
    fun registerModels() {
        itemModIcon.registerItemModel()

        if (ModConfig.Kelp.enabled) {
            if (ModConfig.Kelp.kelpSoupEnabled) itemKelpSoup.registerItemModel()
            if (ModConfig.Kelp.driedKelpEnabled) itemDriedKelp.registerItemModel()
        }

        itemBlockCattail.registerItemModel()
        itemBlockArrowhead.registerItemModel()
    }

    fun initOreDictionary() {
        if (ModConfig.Cornflower.enabled) OreDictionary.registerOre("allFlowers", ModBlocks.blockCornflower)
        if (ModConfig.LilyOfTheValley.enabled) OreDictionary.registerOre("allFlowers", ModBlocks.blockLilyOfTheValley)
        if (ModConfig.WitherRose.enabled) OreDictionary.registerOre("allFlowers", ModBlocks.blockWitherRose)
        if (ModConfig.Kelp.enabled) OreDictionary.registerOre("cropSeaweed", ModBlocks.blockKelp)
    }
}