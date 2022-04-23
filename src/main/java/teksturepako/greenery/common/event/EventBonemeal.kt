package teksturepako.greenery.common.event

import com.charles445.simpledifficulty.api.SDFluids.blockPurifiedWater
import com.charles445.simpledifficulty.api.SDFluids.blockSaltWater
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import teksturepako.greenery.common.block.plant.saltwater.AbstractAquaticPlant
import teksturepako.greenery.common.registry.ModBlocks
import java.util.*

@Mod.EventBusSubscriber
object EventBonemeal {
    private const val SEAGRASS_GENERATION_ATTEMPTS = 8

    @SubscribeEvent(priority = EventPriority.HIGH)
    @JvmStatic fun onBonemealUsedOnGrass(event: BonemealEvent) {
        if (event.block.material == Material.GRASS && event.block.isFullBlock) {
            grow(event.world, event.world.rand, event.pos)
            event.isCanceled = true
        }
    }

    private fun grow(worldIn: World, rand: Random, pos: BlockPos) {
        val defaultPos = pos.up()
        for (i in 0..127) {
            var blockPos = defaultPos
            for (j in 0 until i / 8) {
                blockPos = blockPos.add(
                    rand.nextInt(3) - 1,
                    (rand.nextInt(3) - 1) * rand.nextInt(3) / 2,
                    rand.nextInt(3) - 1
                )
                if (worldIn.getBlockState(blockPos.down()).block !== Blocks.GRASS ||
                    worldIn.getBlockState(blockPos).isNormalCube
                ) continue
            }
            if (worldIn.isAirBlock(blockPos)) {
                if (rand.nextInt(8) == 0) {
                    worldIn.getBiome(blockPos).plantFlower(worldIn, rand, blockPos)
                    spawnParticles(worldIn, blockPos)
                } else {
                    val block = ModBlocks.blockTallGrass
                    val startingAge = rand.nextInt(block.maxAge)
                    val state = block.defaultState.withProperty(block.ageProperty, startingAge)

                    if (block.canBlockStay(worldIn, blockPos, state)) {
                        worldIn.setBlockState(blockPos, state, 3)
                        spawnParticles(worldIn, blockPos)
                    }
                }
            }
        }
    }

    private fun spawnParticles(worldIn: World, pos: BlockPos) {
        if (!worldIn.isAirBlock(pos)) {
            val random = worldIn.rand
            for (i in 0..5) {
                var d1 = (pos.x.toFloat() + random.nextFloat()).toDouble()
                var d2 = (pos.y.toFloat() + random.nextFloat()).toDouble()
                var d3 = (pos.z.toFloat() + random.nextFloat()).toDouble()
                if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube) {
                    d2 = pos.y.toDouble() + 0.0625 + 1.0
                }
                if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube) {
                    d2 = pos.y.toDouble() - 0.0625
                }
                if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube) {
                    d3 = pos.z.toDouble() + 0.0625 + 1.0
                }
                if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube) {
                    d3 = pos.z.toDouble() - 0.0625
                }
                if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube) {
                    d1 = pos.x.toDouble() + 0.0625 + 1.0
                }
                if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube) {
                    d1 = pos.x.toDouble() - 0.0625
                }
                if (d1 < pos.x.toDouble() || d1 > (pos.x + 1).toDouble() || d2 < 0.0 || d2 > (pos.y + 1).toDouble() || d3 < pos.z.toDouble() || d3 > (pos.z + 1).toDouble()) {
                    worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d1, d2, d3, 0.0, 0.0, 0.0)
                }
            }
        }
    }

    @SubscribeEvent
    @JvmStatic fun onBonemealUsedUnderwater(event: BonemealEvent) {
        val world = event.world
        val block = event.block
        val up = event.pos.up()
        val upBlock = world.getBlockState(up).block

        if (Loader.isModLoaded("simpledifficulty")) {
            if ((upBlock == Blocks.WATER || upBlock == blockPurifiedWater) &&
                block.material in AbstractAquaticPlant.ALLOWED_SOILS
            ) {
                if (!world.isRemote) {
                    growRivergrass(up, world)
                    event.result = Event.Result.ALLOW
                } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                    Minecraft.getMinecraft().player.swingArm(event.hand!!)
                }
            }

            if (upBlock == blockSaltWater && block.material in AbstractAquaticPlant.ALLOWED_SOILS) {
                if (!world.isRemote) {
                    growSeagrass(up, world)
                    event.result = Event.Result.ALLOW
                } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                    Minecraft.getMinecraft().player.swingArm(event.hand!!)
                }
            }
        } else {
            if (upBlock == Blocks.WATER && block.material in AbstractAquaticPlant.ALLOWED_SOILS) {
                if (!world.isRemote) {
                    growSeagrass(up, world)
                    event.result = Event.Result.ALLOW
                } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                    Minecraft.getMinecraft().player.swingArm(event.hand!!)
                }
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

            if (Loader.isModLoaded("simpledifficulty")) {
                if (world.getBlockState(blockPos).block == blockSaltWater &&
                    ModBlocks.blockSeagrass.canPlaceBlockAt(world, blockPos)
                ) {
                    world.setBlockState(blockPos, ModBlocks.blockSeagrass.defaultState)
                }
            } else {
                if (world.getBlockState(blockPos).block == Blocks.WATER &&
                    ModBlocks.blockSeagrass.canPlaceBlockAt(world, blockPos)
                ) {
                    world.setBlockState(blockPos, ModBlocks.blockSeagrass.defaultState)
                }
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

            if (Loader.isModLoaded("simpledifficulty")) {
                if ((block == blockPurifiedWater || block == Blocks.WATER)
                    && ModBlocks.blockRivergrass.canPlaceBlockAt(world, blockPos)
                ) {
                    world.setBlockState(blockPos, ModBlocks.blockRivergrass.defaultState)
                }
            } else {
                if (block == Blocks.WATER && ModBlocks.blockRivergrass.canPlaceBlockAt(world, blockPos)) {
                    world.setBlockState(blockPos, ModBlocks.blockRivergrass.defaultState)
                }
            }
        }
    }
}