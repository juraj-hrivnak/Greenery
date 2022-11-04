package teksturepako.greenery.common.block.plant.emergent

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.client.ModSoundTypes
import teksturepako.greenery.common.block.plant.GreeneryPlantBase

abstract class AbstractEmergentPlant(name: String) : GreeneryPlantBase()
{

    companion object
    {
        val ALLOWED_SOILS = setOf<Material>(
            Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK
        )
        val WATER_CROP_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
    }

    init
    {
        setRegistryName("plant/emergent/$name")
        translationKey = name
        soundType = ModSoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionY = entityIn.motionY / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))

        return if ((worldIn.isAirBlock(pos) || worldIn.getBlockState(pos).block == this) && down.material == Material.WATER)
        {
            down2.material in ALLOWED_SOILS
        }
        else false
    }

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return WATER_CROP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(state.getOffset(source, pos))
    }

}