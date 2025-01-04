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

            Greenery.unloadGenerators()
            Greenery.loadPlantGenerators(printParsing =false)
            Greenery.loadArbBlockGenerators(printParsing =false)

            printed = if (!printed)
            {
                GeneratorParser.parseGenerators(
                    generators = plantGenerators.map { it.plant.localizedName to it.plant.worldGen },
                    printParsing = GreeneryConfig.global.printDebugInfo
                )
                GeneratorParser.parseGenerators(
                    generators = arbBlockGenerators.map { it.name to it.worldGen },
                    printParsing = GreeneryConfig.global.printDebugInfo
                )
                true
            }
            else false
        }

    }

}