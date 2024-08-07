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
import teksturepako.greenery.common.block.plant.submerged.SubmergedPlant
import teksturepako.greenery.common.block.plantContainer
import teksturepako.greenery.common.util.MaterialUtil
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*

abstract class TallSubmergedPlant(name: String, maxAge: Int) : SubmergedPlant(name, maxAge)
{
    // -- BLOCK STATE --

    private enum class Variant : IStringSerializable
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

    private val variantProperty: PropertyEnum<Variant> = PropertyEnum.create("variant", Variant::class.java)

    override fun createPlantContainer(): BlockStateContainer = plantContainer(ageProperty, variantProperty)

    init
    {
        initBlockState()
        defaultState = blockState.baseState
            .withProperty(ageProperty, 0)
            .withProperty(variantProperty, Variant.SINGLE)
    }

    // -- BLOCK --

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        return state.withProperty(
            variantProperty, when
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

        val materials = MaterialUtil.materialsOf(allowedSoils)

        return if (down.block == this)      // if block down is weed
        {
            down2.block != this             // if 2 block down is weed return false
        }
        else down.material in materials     // if block down is not weed return if down is in ALLOWED_SOILS
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
        return actualState.getValue(variantProperty) == Variant.SINGLE && canGenerateBlockAt(worldIn, pos.up())
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
            actualState.withProperty(variantProperty, Variant.TOP)    -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(variantProperty, Variant.SINGLE) -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(variantProperty, Variant.BOTTOM) -> BOTTOM_AABB.applyOffset(hasOffset, state, source, pos)
            else                                                      -> TOP_AABB.applyOffset(hasOffset, state, source, pos)
        }
    }
}