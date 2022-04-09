package teksturepako.greenery.proxy

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

interface IProxy {
    class WrongSideException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

    fun preInit(event: FMLPreInitializationEvent)
    fun init(event: FMLInitializationEvent)
    fun postInit(event: FMLPostInitializationEvent)

    fun registerItemRenderer(item: Item, meta: Int, id: String)
    fun registerItemBlockRenderer(itemBlock: Item, meta: Int, id: String)

    fun registerBlockColourHandlers(block: Block, event: ColorHandlerEvent.Block)
    fun registerItemColourHandlers(item: Item, event: ColorHandlerEvent.Item)

}
