@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.floating

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
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
import teksturepako.greenery.common.block.plantContainer
import teksturepako.greenery.common.util.FluidUtil
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*

abstract class FloatingPlant(val name: String, maxAge: Int) : GreeneryPlant(maxAge)
{
    abstract var compatibleFluids: List<String>

    companion object
    {
        val WATER_CROP_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
    }

    // -- BLOCK STATE --

    private val frostedProperty: PropertyBool = PropertyBool.create("frosted")

    override fun createPlantContainer(): BlockStateContainer = plantContainer(ageProperty, frostedProperty)

    init
    {
        initBlockState()
        defaultState = blockState.baseState
            .withProperty(ageProperty, 0)
            .withProperty(frostedProperty, false)
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

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState = when
    {
        worldIn.getBlockState(pos.down()).material == Material.ICE -> state.withProperty(frostedProperty, true)
        else -> state.withProperty(frostedProperty, false)
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX /= 1.1
        entityIn.motionY /= 1.1
        entityIn.motionZ /= 1.1
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val isPosValid = worldIn.isAirBlock(pos) || worldIn.getBlockState(pos).block == this

        val isFluidValid = FluidUtil.canGenerateOnFluids(compatibleFluids, worldIn, pos.down())
                           || worldIn.getBlockState(pos.down()).material == Material.ICE

        return isPosValid && isFluidValid
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        val state = this.defaultState

        if (!canBlockStay(world, pos, state)) return

        world.setBlockState(pos, state, flags)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return WATER_CROP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
    }
}