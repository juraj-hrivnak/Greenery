package teksturepako.greenery.common.handler.event

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.util.ConfigUtil

@Mod.EventBusSubscriber
object EventConfigChanged
{
    private var printed = false

    @SubscribeEvent
    @JvmStatic
    fun onConfigChanged(event: ConfigChangedEvent)
    {
        if (event.modID == Greenery.MODID)
        {
            ConfigManager.sync(Greenery.MODID, Config.Type.INSTANCE)

            Greenery.generators.clear()
            Greenery.loadGenerators(false)

            printed = if (!printed)
            {
                ConfigUtil.parseGenerators(Greenery.generators, true)
                true
            }
            else false
        }

    }

}