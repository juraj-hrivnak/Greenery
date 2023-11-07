@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.emergent

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fluids.Fluid
import teksturepako.greenery.Greenery
import teksturepako.greenery.client.GreenerySoundTypes
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.block.plant.PlantDamageSource.Companion.Prickly
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.FluidUtil
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*

abstract class EmergentPlantBase(val name: String) : GreeneryPlant(), IFluidloggable
{
    abstract var compatibleFluids: MutableList<String>

    companion object
    {
        val ALLOWED_SOILS = setOf<Material>(Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK)
        val WATER_CROP_TOP_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
        val WATER_CROP_BOTTOM_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.0, 0.9)
        )
        val TOP: PropertyBool = PropertyBool.create("top")
    }

    init
    {
        setRegistryName("plant/emergent/$name")
        translationKey = "${Greenery.MODID}.$name"
        soundType = GreenerySoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab

        defaultState = blockState.baseState.withProperty(AGE, 0).withProperty(TOP, true)
    }

    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, AGE, TOP)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState
    {
        return when
        {
            worldIn.getBlockState(pos.down()).block == this -> state.withProperty(TOP, true)
            worldIn.getBlockState(pos.up()).block == this -> state.withProperty(TOP, false)
            else -> state // Keep the same state when breaking the block.
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return when (val actualState = getActualState(state, source, pos))
        {
            actualState.withProperty(TOP, true) -> WATER_CROP_TOP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
            actualState.withProperty(TOP, false) -> WATER_CROP_BOTTOM_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
            else -> WATER_CROP_TOP_AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
        }
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX /= (Config.global.slowdownModifier * 0.1 + 1)
        entityIn.motionY /= (Config.global.slowdownModifier * 0.1 + 1)
        entityIn.motionZ /= (Config.global.slowdownModifier * 0.1 + 1)

        if (isHarmful && entityIn is EntityPlayer)
        {
            if (entityIn.inventory.armorInventory[0] == ItemStack.EMPTY)
            {
                entityIn.attackEntityFrom(Prickly(this.localizedName), 0.5f)
            }
            if (entityIn.inventory.armorInventory[1] == ItemStack.EMPTY)
            {
                entityIn.attackEntityFrom(Prickly(this.localizedName), 0.5f)
            }
        }
    }

    /**
     * Determines whether the block can be generated on the position, based on [canBlockStay] and [FluidUtil.canGenerateInFluids].
     */
    private fun canGenerateBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        return (FluidUtil.canGenerateInFluids(compatibleFluids, worldIn, pos)
                && canBlockStay(worldIn, pos, defaultState))
               && worldIn.isAirBlock(pos.up())
    }

    /**
     * Checks if there is a compatible fluid block on the placing position.
     * Warning: This should not be used in world generation because of performance reasons!
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        val fluidState = FluidloggedUtils.getFluidState(worldIn, pos)
        return if (!fluidState.isEmpty && isFluidValid(defaultState, worldIn, pos, fluidState.fluid)
                   && FluidloggedUtils.isFluidloggableFluid(fluidState.state, worldIn, pos))
        {
            canBlockStay(worldIn, pos, defaultState) && worldIn.isAirBlock(pos.up())
        }
        else
        {
            canBlockStay(worldIn, pos.up(), defaultState)
        }
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack)
    {
        val down = worldIn.getBlockState(pos.down())
        if (down.block != this) worldIn.setBlockState(pos, this.defaultState.withProperty(AGE, 3))
        if (this.canBlockStay(worldIn, pos.up(), state))
        {
            worldIn.setBlockState(pos.up(), this.defaultState, 2)
        }
    }

    override fun onBlockHarvested(worldIn: World, pos: BlockPos, state: IBlockState, player: EntityPlayer)
    {
        if (worldIn.getBlockState(pos.down()).block == this)
        {
            dropBlockAsItem(worldIn, pos.down(), state, 0)
            worldIn.setBlockToAir(pos.down())
        }
    }

    override fun checkAndDropBlock(worldIn: World, pos: BlockPos, state: IBlockState)
    {
        if (!canBlockStay(worldIn, pos, state))
        {
            worldIn.setBlockToAir(pos)
        }
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))
        return if (down.material in ALLOWED_SOILS) true
        else down.block == this && down2.block != this
    }

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        if (this.canGenerateBlockAt(world, pos))
        {
            val state = this.defaultState

            world.setBlockState(pos, state.withProperty(this.ageProperty, maxAge), flags)

            if (this.canBlockStay(world, pos.up(), state))
            {
                world.setBlockState(pos.up(), state, flags)
            }
        }
    }

    override fun canFluidFlow(world: IBlockAccess, pos: BlockPos, here: IBlockState, side: EnumFacing): Boolean = true

    override fun isFluidValid(state: IBlockState, world: World, pos: BlockPos, fluid: Fluid): Boolean
    {
        return FluidUtil.areFluidsValid(compatibleFluids, fluid)
    }

    override fun onFluidDrain(world: World, pos: BlockPos, here: IBlockState, blockFlags: Int): EnumActionResult
    {
        world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, getStateId(here))
        dropBlockAsItem(world, pos, here, 0)
        world.setBlockState(pos, Blocks.AIR.defaultState, blockFlags)
        return EnumActionResult.SUCCESS
    }
}