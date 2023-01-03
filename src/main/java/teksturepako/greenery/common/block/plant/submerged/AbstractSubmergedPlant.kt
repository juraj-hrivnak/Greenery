@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.submerged

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraft.block.Block
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import teksturepako.greenery.client.GreenerySoundTypes
import teksturepako.greenery.common.block.ModMaterials
import teksturepako.greenery.common.util.FluidUtil
import java.util.*

abstract class AbstractSubmergedPlant(name: String) : Block(ModMaterials.AQUATIC_PLANT), IGrowable, IFluidloggable
{

    companion object
    {
        val ALLOWED_SOILS = setOf<Material>(
            Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK
        )
        val TOP_AABB = AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9)
        val BOTTOM_AABB = AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.00, 0.9)
    }

    abstract val compatibleFluids: MutableList<String>

    lateinit var itemBlock: Item

    init
    {
        setRegistryName("plant/submerged/$name")
        translationKey = name
        soundType = GreenerySoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab
        lightOpacity = 2
    }

    fun createItemBlock(): Item
    {
        itemBlock = ItemBlock(this).setRegistryName(registryName).setTranslationKey(translationKey)
        return itemBlock
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())
    }

    override fun getCollisionBoundingBox(state: IBlockState, world: IBlockAccess, pos: BlockPos): AxisAlignedBB?
    {
        return NULL_AABB
    }

    //Rendering
    @SideOnly(Side.CLIENT)
    override fun getRenderLayer(): BlockRenderLayer
    {
        return BlockRenderLayer.CUTOUT
    }

    override fun isFullCube(state: IBlockState): Boolean
    {
        return false
    }

    override fun isOpaqueCube(state: IBlockState): Boolean
    {
        return false
    }

    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape
    {
        return BlockFaceShape.UNDEFINED
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionY = entityIn.motionY / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
    }

    override fun isReplaceable(world: IBlockAccess, pos: BlockPos): Boolean
    {
        return false
    }

    /**
     * Determines whether the block can stay on the position, based on its surroundings.
     */
    abstract fun canBlockStay(worldIn: World, pos: BlockPos): Boolean

    /**
     * Determines whether the block can be generated on the position, based on [canBlockStay] and [FluidUtil.canGenerateInFluids].
     */
    fun canGenerateBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        return FluidUtil.canGenerateInFluids(compatibleFluids, worldIn, pos) && canBlockStay(worldIn, pos)
    }

    /**
     * Checks if there is a compatible fluid block on the placing position.
     * Warning: This should not be used in world generation because of performance reasons!
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        val fluidState = FluidloggedUtils.getFluidState(worldIn, pos)
        return (!fluidState.isEmpty && isFluidValid(
            defaultState, worldIn, pos, fluidState.fluid
        ) && FluidloggedUtils.isFluidloggableFluid(fluidState.state, worldIn, pos) && canBlockStay(worldIn, pos))
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos)
    {
        checkAndDropBlock(worldIn, pos, state)
    }

    private fun checkAndDropBlock(worldIn: World, pos: BlockPos, state: IBlockState)
    {
        if (!canBlockStay(worldIn, pos))
        {
            dropBlockAsItem(worldIn, pos, state, 0)
            worldIn.setBlockToAir(pos)
        }
    }

    // IGrowable implementation
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean
    {
        return canGrow(worldIn, pos, state, false)
    }

    override fun canFluidFlow(world: IBlockAccess, pos: BlockPos, here: IBlockState, side: EnumFacing): Boolean
    {
        return true
    }

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