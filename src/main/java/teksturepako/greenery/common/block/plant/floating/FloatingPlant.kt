@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.floating

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.client.GreenerySoundTypes
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.util.Utils.applyOffset

abstract class FloatingPlant(val name: String, maxAge: Int) : GreeneryPlant(maxAge)
{
    companion object
    {
        val WATER_CROP_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
    }

    // -- BLOCK --

    init
    {
        setRegistryName("plant/floating/$name")
        translationKey = "${Greenery.MODID}.$name"
        soundType = GreenerySoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab
    }

    override fun createItemBlock(): Item
    {
        itemBlock = FloatingItemBlock(name, this)
        return itemBlock
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX /= 1.1
        entityIn.motionY /= 1.1
        entityIn.motionZ /= 1.1
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val down = worldIn.getBlockState(pos.down())

        return (worldIn.isAirBlock(pos) || worldIn.getBlockState(pos).block == this) && down.material == Material.WATER
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return WATER_CROP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
    }
}