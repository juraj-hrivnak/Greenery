package teksturepako.greenery.common.event

import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.common.config.Config

object EventWorldGen {
    @SubscribeEvent(priority = EventPriority.HIGH)
    @JvmStatic
    fun removeGrass(event: Decorate) {
        if (event.type == EventType.GRASS && Config.generation.removeGrass) {
            event.result = Event.Result.DENY
        }
    }
}