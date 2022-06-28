package teksturepako.greenery.common.block.crop

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.client.ModSoundTypes
import teksturepako.greenery.common.block.GreeneryCropBase
import teksturepako.greenery.common.block.plant.freshwater.AbstractAquaticPlant

abstract class AbstractWaterCropBlock(name: String) : GreeneryCropBase() {

    companion object {
        val ALLOWED_SOILS = setOf<Material>(
            Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK
        )
        val WATER_CROP_AABB = arrayOf(
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.5, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.625, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.75, 0.899999988079071),
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.875, 0.899999988079071)
        )
    }

    init {
        setRegistryName(name)
        translationKey = name
        soundType = ModSoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionY = entityIn.motionY / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))

        return if ((worldIn.isAirBlock(pos)
                    || worldIn.getBlockState(pos).block == this)
            && down.material == Material.WATER
        ) {
            down2.material in ALLOWED_SOILS
        } else false
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return WATER_CROP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(state.getOffset(source, pos))
    }

}