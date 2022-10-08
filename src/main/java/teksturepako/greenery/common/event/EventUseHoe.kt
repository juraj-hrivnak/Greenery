package teksturepako.greenery.common.event

import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumHand
import net.minecraftforge.event.entity.player.UseHoeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.common.registry.ModBlocks

@Mod.EventBusSubscriber
object EventUseHoe {
    @SubscribeEvent
    @JvmStatic
    fun onHoeUsedOnGrass(event: UseHoeEvent) {
        if (event.world.getBlockState(event.pos).block == ModBlocks.blockGrass && event.world.isAirBlock(event.pos.up())) {
            event.world.setBlockState(event.pos, Blocks.FARMLAND.defaultState)
            event.entityPlayer.swingArm(EnumHand.MAIN_HAND)
            event.entityPlayer.playSound(SoundEvents.ITEM_HOE_TILL, 1.0f, 1.0f)
            event.result = Event.Result.ALLOW
        }
    }
}