package teksturepako.greenery.common.event

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.Greenery

@Mod.EventBusSubscriber
object EventConfigChanged {

    @SubscribeEvent
    @JvmStatic
    fun onConfigChanged(event: ConfigChangedEvent) {
        if (event.modID == Greenery.MODID) {
            ConfigManager.sync(Greenery.MODID, Config.Type.INSTANCE)

            Greenery.generators.clear()
            Greenery.loadGenerators()
        }

    }

}