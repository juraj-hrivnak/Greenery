package teksturepako.greenery.common.event

import net.minecraft.entity.boss.EntityWither
import net.minecraft.item.ItemStack
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.registry.ModBlocks

@Mod.EventBusSubscriber
object EventLivingDeath {
    @SubscribeEvent
    @JvmStatic fun onEntityKilledByWither(event: LivingDeathEvent) {
        if (!ModConfig.WitherRose.enabled || !ModConfig.WitherRose.dropFromWitherKills) return

        val entity = event.entityLiving
        val world = entity.world

        if (!world.isRemote && entity.attackingEntity is EntityWither) {
            var placed = false

            if (ForgeEventFactory.getMobGriefingEvent(world, entity)) {
                val pos = entity.position
                if (world.isAirBlock(pos) && ModBlocks.blockWitherRose.canPlaceBlockAt(world, pos)) {
                    world.setBlockState(pos, ModBlocks.blockWitherRose.defaultState, 3)
                    placed = true
                }
            }

            if (!placed) {
                entity.entityDropItem(ItemStack(ModBlocks.blockWitherRose), 1f)
            }
        }
    }
}