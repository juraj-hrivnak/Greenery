package teksturepako.greenery.common.event

import com.charles445.simpledifficulty.api.SDFluids.blockPurifiedWater
import com.charles445.simpledifficulty.api.SDFluids.blockSaltWater
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import teksturepako.greenery.common.block.plant.aquatic.AbstractAquaticPlant
import teksturepako.greenery.common.registry.ModBlocks
import java.util.*

@Mod.EventBusSubscriber
object EventBonemeal {
    private const val GENERATION_ATTEMPTS = 8

    private fun spawnParticles(worldIn: World, pos: BlockPos, rand: Random) {
        for (i in 0 until GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(2) - rand.nextInt(2),
                rand.nextInt(4) - rand.nextInt(4)
            )

            var d1 = (blockPos.x.toFloat() + rand.nextFloat()).toDouble()
            var d2 = (blockPos.y.toFloat() + rand.nextFloat()).toDouble()
            var d3 = (blockPos.z.toFloat() + rand.nextFloat()).toDouble()
            when {
                i == 0 && !worldIn.getBlockState(blockPos.up()).isOpaqueCube     -> d2 = blockPos.y.toDouble() + 0.0625 + 1.0
                i == 1 && !worldIn.getBlockState(blockPos.down()).isOpaqueCube   -> d2 = blockPos.y.toDouble() - 0.0625
                i == 2 && !worldIn.getBlockState(blockPos.south()).isOpaqueCube  -> d3 = blockPos.z.toDouble() + 0.0625 + 1.0
                i == 3 && !worldIn.getBlockState(blockPos.north()).isOpaqueCube  -> d3 = blockPos.z.toDouble() - 0.0625
                i == 4 && !worldIn.getBlockState(blockPos.east()).isOpaqueCube   -> d1 = blockPos.x.toDouble() + 0.0625 + 1.0
                i == 5 && !worldIn.getBlockState(blockPos.west()).isOpaqueCube   -> d1 = blockPos.x.toDouble() - 0.0625
            }
            if (d1 < blockPos.x.toDouble() || d1 > (blockPos.x + 1).toDouble() || d2 < 0.0 || d2 > (blockPos.y + 1).toDouble() || d3 < blockPos.z.toDouble() || d3 > (blockPos.z + 1).toDouble()) {
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d1, d2, d3, 0.0, 0.0, 0.0)
            }
        }
    }

    @SubscribeEvent
    @JvmStatic fun onBonemealUsed(event: BonemealEvent) {
        val world = event.world
        val block = event.block
        val up = event.pos.up()
        val upBlock = world.getBlockState(up).block
        val rand = world.rand

        if (event.block.material == Material.GRASS && event.block.isFullBlock) {
            if (!world.isRemote) {
                event.result = Event.Result.ALLOW
                event.stack.count -= 1
                growGrass(up, world, rand)
            } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                Minecraft.getMinecraft().player.swingArm(event.hand!!)
                spawnParticles(world, up, rand)
            }
            event.isCanceled = true
        }

        if (Loader.isModLoaded("simpledifficulty")) {
            if ((upBlock == Blocks.WATER || upBlock == blockPurifiedWater) &&
                block.material in AbstractAquaticPlant.ALLOWED_SOILS
            ) {
                if (!world.isRemote) {
                    growRivergrass(up, world, rand)
                    event.result = Event.Result.ALLOW
                } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                    Minecraft.getMinecraft().player.swingArm(event.hand!!)
                    spawnParticles(world, up, rand)
                }
            }

            if (upBlock == blockSaltWater && block.material in AbstractAquaticPlant.ALLOWED_SOILS) {
                if (!world.isRemote) {
                    growSeagrass(up, world, rand)
                    event.result = Event.Result.ALLOW
                } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                    Minecraft.getMinecraft().player.swingArm(event.hand!!)
                    spawnParticles(world, up, rand)
                }
            }
        } else {
            if (upBlock == Blocks.WATER && block.material in AbstractAquaticPlant.ALLOWED_SOILS) {
                if (!world.isRemote) {
                    growSeagrass(up, world, rand)
                    event.result = Event.Result.ALLOW
                } else if (event.entityPlayer == Minecraft.getMinecraft().player) {
                    Minecraft.getMinecraft().player.swingArm(event.hand!!)
                    spawnParticles(world, up, rand)
                }
            }
        }
    }

    private fun growSeagrass(pos: BlockPos, world: World, rand: Random) {
        for (i in 0 until GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(2) - rand.nextInt(2),
                rand.nextInt(4) - rand.nextInt(4)
            )

            if (Loader.isModLoaded("simpledifficulty")) {
                if (world.getBlockState(blockPos).block == blockSaltWater &&
                    ModBlocks.blockSeagrass.canGenerateBlockAt(world, blockPos)
                ) {
                    world.setBlockState(blockPos, ModBlocks.blockSeagrass.defaultState)
                }
            } else {
                if (world.getBlockState(blockPos).block == Blocks.WATER &&
                    ModBlocks.blockSeagrass.canGenerateBlockAt(world, blockPos)
                ) {
                    world.setBlockState(blockPos, ModBlocks.blockSeagrass.defaultState)
                }
            }
        }
    }

    private fun growRivergrass(pos: BlockPos, world: World, rand: Random) {
        for (i in 0 until GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(2) - rand.nextInt(2),
                rand.nextInt(4) - rand.nextInt(4)
            )
            val block = world.getBlockState(blockPos).block

            if (Loader.isModLoaded("simpledifficulty")) {
                if ((block == blockPurifiedWater || block == Blocks.WATER)
                    && ModBlocks.blockRivergrass.canGenerateBlockAt(world, blockPos)
                ) {
                    world.setBlockState(blockPos, ModBlocks.blockRivergrass.defaultState)
                }
            } else {
                if (block == Blocks.WATER && ModBlocks.blockRivergrass.canGenerateBlockAt(world, blockPos)) {
                    world.setBlockState(blockPos, ModBlocks.blockRivergrass.defaultState)
                }
            }
        }
    }

    private fun growGrass(pos: BlockPos, world: World, rand: Random) {
        for (i in 0 until GENERATION_ATTEMPTS) {
            val blockPos = pos.add(
                rand.nextInt(4) - rand.nextInt(4),
                rand.nextInt(2) - rand.nextInt(2),
                rand.nextInt(4) - rand.nextInt(4)
            )

            if (ModBlocks.blockTallGrass.canPlaceBlockAt(world, blockPos)) {
                world.notifyBlockUpdate(blockPos, world.getBlockState(blockPos), ModBlocks.blockTallGrass.defaultState, Constants.BlockFlags.DEFAULT_AND_RERENDER)
                world.setBlockState(blockPos, ModBlocks.blockTallGrass.defaultState)
            }
        }
    }
}