@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.submerged.tall

import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.common.block.plant.submerged.AbstractSubmergedPlant
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*

abstract class TallSubmergedPlantBase(name: String) : AbstractSubmergedPlant(name)
{
    enum class Variant : IStringSerializable
    {
        SINGLE, BOTTOM, TOP;

        override fun getName(): String
        {
            return name.lowercase(Locale.getDefault())
        }

        override fun toString(): String
        {
            return getName()
        }
    }

    companion object
    {
        val VARIANT: PropertyEnum<Variant> = PropertyEnum.create("variant", Variant::class.java)
    }

    init
    {
        defaultState = blockState.baseState
                .withProperty(VARIANT, Variant.SINGLE)
                .withProperty(AGE, 0)
    }

    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, VARIANT, AGE)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        return state.withProperty(
            VARIANT, when
            {
                worldIn.getBlockState(pos.down()).block == this -> Variant.TOP
                worldIn.getBlockState(pos.up()).block == this -> Variant.BOTTOM
                else -> Variant.SINGLE
            }
        )
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        //Must have a SINGLE weed or valid soil below
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))
        return if (down.block == this)      // if block down is weed
        {
            down2.block != this             // if 2 block down is weed return false
        }
        else down.material in ALLOWED_SOILS // if block down is not weed return if down is in ALLOWED_SOILS
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        if (this.canGenerateBlockAt(world, pos))
        {
            val state = this.defaultState
            world.setBlockState(pos, state, flags)

            if (rand.nextDouble() < 0.05 && this.canGenerateBlockAt(world, pos.up()))
            {
                world.setBlockState(pos.up(), state, flags)
            }
        }
    }

    // IGrowable implementation
    override fun canGrow(worldIn: World, pos: BlockPos, state: IBlockState, isClient: Boolean): Boolean
    {
        val actualState = state.getActualState(worldIn, pos)
        return actualState.getValue(VARIANT) == Variant.SINGLE && canGenerateBlockAt(worldIn, pos.up())
    }

    override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState)
    {
        if (canGrow(worldIn, pos, state, false))
        {
            worldIn.setBlockState(pos.up(), state)
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return when (val actualState = getActualState(state, source, pos))
        {
            actualState.withProperty(VARIANT, Variant.TOP) -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(VARIANT, Variant.SINGLE) -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(VARIANT, Variant.BOTTOM) -> BOTTOM_AABB.applyOffset(hasOffset, state, source, pos)
            else -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
        }
    }
}