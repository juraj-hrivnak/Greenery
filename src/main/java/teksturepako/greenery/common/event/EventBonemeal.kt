package teksturepako.greenery.common.event

import com.charles445.simpledifficulty.api.SDFluids.blockPurifiedWater
import com.charles445.simpledifficulty.api.SDFluids.blockSaltWater
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.block.flower.AbstractFlower
import teksturepako.greenery.common.block.plant.saltwater.AbstractAquaticPlant
import teksturepako.greenery.common.registry.ModBlocks

@Mod.EventBusSubscriber
object EventBonemeal {
    private const val FLOWER_GENERATION_ATTEMPTS = 4
    private const val SEAGRASS_GENERATION_ATTEMPTS = 8

    @SubscribeEvent
    @JvmStatic fun onBonemealUsedOnDirt(event: BonemealEvent) {
        val world = event.world
        if (world.isRemote) return

        val block = event.block
        val up = event.pos.up()

        if (world.isAirBlock(up) && block.material == Material.GRASS) {
            if (ModConfig.Cornflower.enabled) {
                growFlowers(up, world, ModBlocks.blockCornflower)
            }
            if (ModConfig.LilyOfTheValley.enabled) {
                growFlowers(up, world, ModBlocks.blockLilyOfTheValley)
            }
            event.result = Event.Result.DEFAULT
        }
    }

    @SubscribeEvent
    @JvmStatic fun onBonemealUsedOnSoulsand(event: BonemealEvent) {
        if (!ModConfig.WitherRose.enabled) return

        val world = event.world
        val block = event.block
        val up = event.pos.up()

        if (world.isAirBlock(up) && block.block == Blocks.SOUL_SAND) {
            if (!world.isRemote) {
                growFlowers(up, world, ModBlocks.blockWitherRose)
                event.result = Event.Result.ALLOW
            }
            else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                Minecraft.getMinecraft().player.swingArm(event.hand!!)
            }
        }
    }

    @SubscribeEvent
    @JvmStatic fun onBonemealUsedUnderwater(event: BonemealEvent) {
        if (!ModConfig.Seagrass.enabled) return

        val world = event.world
        val block = event.block
        val up = event.pos.up()
        val upBlock = world.getBlockState(up).block

        if ((upBlock == Blocks.WATER || upBlock == blockPurifiedWater) &&
            block.material in AbstractAquaticPlant.ALLOWED_SOILS
        ) {
            if (!world.isRemote) {
                growRivergrass(up, world)
                event.result = Event.Result.ALLOW
            }
            else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                Minecraft.getMinecraft().player.swingArm(event.hand!!)
            }
        }

        if (upBlock == blockSaltWater && block.material in AbstractAquaticPlant.ALLOWED_SOILS) {
            if (!world.isRemote) {
                growSeagrass(up, world)
                event.result = Event.Result.ALLOW
            }
            else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                Minecraft.getMinecraft().player.swingArm(event.hand!!)
            }
        }
    }

    private fun growFlowers(pos: BlockPos, world: World, flower: AbstractFlower) {
        val rand = world.rand

        for (i in 0 until FLOWER_GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                    rand.nextInt(3) - rand.nextInt(3),
                    rand.nextInt(2) - rand.nextInt(2),
                    rand.nextInt(3) - rand.nextInt(3)
            )

            if (world.isAirBlock(blockPos)
                    && flower.canPlaceBlockAt(world, blockPos)
                    && flower.isBiomeValid(world.getBiome(blockPos))) {
                world.setBlockState(blockPos, flower.defaultState)
            }
        }
    }

    private fun growSeagrass(pos: BlockPos, world: World) {
        val rand = world.rand

        for (i in 0 until SEAGRASS_GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(2) - rand.nextInt(2),
                rand.nextInt(4) - rand.nextInt(4)
            )

            if (world.getBlockState(blockPos).block == blockSaltWater &&
                ModBlocks.blockSeagrass.canPlaceBlockAt(world, blockPos)
            ) {
                world.setBlockState(blockPos, ModBlocks.blockSeagrass.defaultState)
            }
        }
    }

    private fun growRivergrass(pos: BlockPos, world: World) {
        val rand = world.rand

        for (i in 0 until SEAGRASS_GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(2) - rand.nextInt(2),
                    rand.nextInt(4) - rand.nextInt(4)
            )
            val block = world.getBlockState(blockPos).block

            if ((block == blockPurifiedWater || block == Blocks.WATER)
                && ModBlocks.blockRivergrass.canPlaceBlockAt(world, blockPos)
            ) {
                world.setBlockState(blockPos, ModBlocks.blockRivergrass.defaultState)
            }
        }
    }
}