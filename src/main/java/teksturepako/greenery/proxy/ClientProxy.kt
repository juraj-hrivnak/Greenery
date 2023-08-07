@file:Suppress("DEPRECATION")

package teksturepako.greenery.proxy

import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.BlockColors
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.world.ColorizerGrass
import net.minecraft.world.biome.BiomeColorHelper
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.registry.ModItems

@Mod.EventBusSubscriber(Side.CLIENT)
class ClientProxy : IProxy
{
    companion object
    {
        @SubscribeEvent
        @JvmStatic
        fun registerModels(event: ModelRegistryEvent)
        {
            Greenery.logger.info("Registering models")
            ModBlocks.registerModels()
            ModItems.registerModels()
        }

        @SubscribeEvent
        @JvmStatic
        fun registerBlockColorHandlers(event: ColorHandlerEvent.Block)
        {
            ModBlocks.registerBlockColorHandlers(event)
        }

        @SubscribeEvent
        @JvmStatic
        fun registerItemColorHandlers(event: ColorHandlerEvent.Item)
        {
            ModBlocks.registerItemBlockColorHandlers(event)
        }
    }

    override fun preInit(event: FMLPreInitializationEvent)
    {

    }

    override fun init(event: FMLInitializationEvent)
    {

    }

    override fun postInit(event: FMLPostInitializationEvent)
    {

    }

    override fun registerItemRenderer(item: Item, meta: Int, id: String)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, ModelResourceLocation(id))
    }

    override fun registerItemBlockRenderer(itemBlock: Item, meta: Int, id: String)
    {
        ModelLoader.setCustomModelResourceLocation(itemBlock, meta, ModelResourceLocation(id, "inventory"))
    }

    override fun registerGrassColorHandler(block: Block, event: ColorHandlerEvent.Block)
    {
        val blockColors: BlockColors = event.blockColors
        val grassColourHandler = IBlockColor { _, blockAccess, pos, _ ->
            if (blockAccess != null && pos != null)
            {
                BiomeColorHelper.getGrassColorAtPos(blockAccess, pos)
            }
            else ColorizerGrass.getGrassColor(0.5, 1.0)
        }
        blockColors.registerBlockColorHandler(grassColourHandler, block)
    }

    override fun registerItemColorHandler(item: Item, event: ColorHandlerEvent.Item)
    {
        val itemBlockColourHandler = IItemColor { stack: ItemStack, tintIndex: Int ->
            val state = (stack.item as ItemBlock).block.getStateFromMeta(stack.metadata)
            event.blockColors.colorMultiplier(state, null, null, tintIndex)
        }
        event.itemColors.registerItemColorHandler(itemBlockColourHandler, item)
    }
}
