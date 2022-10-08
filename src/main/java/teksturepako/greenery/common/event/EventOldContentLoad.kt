package teksturepako.greenery.common.event

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries

@Mod.EventBusSubscriber
object EventOldContentLoad {

    private val oldContent: Map<String, String> = mapOf(
        "greenery:arrowhead"    to "greenery:plant/emergent/arrowhead",
        "greenery:cattail"      to "greenery:plant/emergent/cattail",

        "greenery:kelp"         to "greenery:plant/submerged/kelp",
        "greenery:rivergrass"   to "greenery:plant/submerged/rivergrass",
        "greenery:seagrass"     to "greenery:plant/submerged/seagrass",

        "greenery:barley"       to "greenery:plant/upland/tall/barley",
        "greenery:nettle"       to "greenery:plant/upland/tall/nettle",
        "greenery:ryegrass"     to "greenery:plant/upland/tall/ryegrass",
        "greenery:tallfern"     to "greenery:plant/upland/tall/tallfern",
        "greenery:tallgrass"    to "greenery:plant/upland/tall/tallgrass"
    )

    @SubscribeEvent
    @JvmStatic
    fun onOldBlocksLoad(event: RegistryEvent.MissingMappings<Block>) {
        for (mapping in event.allMappings) {
            val key = mapping.key
            if (key.namespace == "greenery") {
                val block = ForgeRegistries.BLOCKS.getValue(ResourceLocation(oldContent[key.toString()] ?: continue))
                mapping.remap(block)
            }
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onOldItemsLoad(event: RegistryEvent.MissingMappings<Item>) {
        for (mapping in event.allMappings) {
            val key = mapping.key
            if (key.namespace == "greenery") {
                val item = ForgeRegistries.ITEMS.getValue(ResourceLocation(oldContent[key.toString()] ?: continue))
                mapping.remap(item)
            }
        }
    }

}