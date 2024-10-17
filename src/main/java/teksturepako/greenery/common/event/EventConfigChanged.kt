package teksturepako.greenery.common.event

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.Greenery
import teksturepako.greenery.Greenery.arbBlockGenerators
import teksturepako.greenery.Greenery.plantGenerators
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

            Greenery.plantGenerators.clear()
            Greenery.loadPlantGenerators(false)

            Greenery.arbBlockGenerators.clear()
            Greenery.loadArbBlockGenerators(false)

            printed = if (!printed)
            {
                ConfigUtil.parseGenerators(plantGenerators.map { it.plant.localizedName to it.plant.worldGen }, true)
                ConfigUtil.parseGenerators(arbBlockGenerators.map { it.name to it.worldGen }, true)
                true
            }
            else false
        }

    }

}