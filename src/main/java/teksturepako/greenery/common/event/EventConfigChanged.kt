package teksturepako.greenery.common.event

import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.Greenery
import teksturepako.greenery.Greenery.arbBlockGenerators
import teksturepako.greenery.Greenery.plantGenerators
import teksturepako.greenery.common.config.parser.GeneratorParser
import teksturepako.greenery.common.config.Config as GreeneryConfig

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
            Greenery.loadPlantGenerators(GreeneryConfig.global.printDebugInfo)

            Greenery.arbBlockGenerators.clear()
            Greenery.loadArbBlockGenerators(GreeneryConfig.global.printDebugInfo)

            printed = if (!printed)
            {
                GeneratorParser.parseGenerators(plantGenerators.map { it.plant.localizedName to it.plant.worldGen }, GreeneryConfig.global.printDebugInfo)
                GeneratorParser.parseGenerators(arbBlockGenerators.map { it.name to it.worldGen }, GreeneryConfig.global.printDebugInfo)
                true
            }
            else false
        }

    }

}