package teksturepako.greenery.proxy

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber(Side.SERVER)
class ServerProxy : IProxy
{
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
        throw IProxy.WrongSideException("Tried to call ${::registerItemRenderer.name} on server")
    }

    override fun registerItemBlockRenderer(itemBlock: Item, meta: Int, id: String)
    {
        throw IProxy.WrongSideException("Tried to call ${::registerItemBlockRenderer.name} on server")
    }

    override fun registerGrassColourHandler(block: Block, event: ColorHandlerEvent.Block)
    {
        throw IProxy.WrongSideException("Tried to call ${::registerGrassColourHandler.name} on server")
    }

    override fun registerItemColourHandler(item: Item, event: ColorHandlerEvent.Item)
    {
        throw IProxy.WrongSideException("Tried to call ${::registerItemColourHandler.name} on server")
    }

}