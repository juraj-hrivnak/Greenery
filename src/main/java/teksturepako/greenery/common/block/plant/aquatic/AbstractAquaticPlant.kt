package teksturepako.greenery.common.block.plant.aquatic

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraft.block.Block
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
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
import teksturepako.greenery.client.ModMaterials
import teksturepako.greenery.client.ModSoundTypes
import teksturepako.greenery.common.util.FluidUtil
import java.util.*

abstract class AbstractAquaticPlant(name: String) : Block(ModMaterials.AQUATIC_PLANT), IGrowable, IFluidloggable {

    companion object {
        val ALLOWED_SOILS = setOf<Material>(
            Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK
        )
        val TOP_AABB =
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 0.75, 0.899999988079071)
        val BOTTOM_AABB =
            AxisAlignedBB(0.10000001192092896, 0.025, 0.10000001192092896, 0.899999988079071, 1.0, 0.899999988079071)
    }

    abstract val compatibleFluids: MutableList<String>

    lateinit var itemBlock: Item

    init {
        setRegistryName(name)
        translationKey = name
        soundType = ModSoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab
        lightOpacity = 2
    }

    fun createItemBlock(): Item {
        itemBlock = ItemBlock(this).setRegistryName(registryName).setTranslationKey(translationKey)
        return itemBlock
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel() {
        Greenery.proxy.registerItemBlockRenderer(itemBlock, 0, registryName.toString())
    }

    @Deprecated("", ReplaceWith("NULL_AABB", "net.minecraft.block.Block.NULL_AABB"))
    override fun getCollisionBoundingBox(state: IBlockState, world: IBlockAccess, pos: BlockPos): AxisAlignedBB? {
        return NULL_AABB
    }

    //Rendering
    @SideOnly(Side.CLIENT)
    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun isOpaqueCube(state: IBlockState): Boolean {
        return false
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionY = entityIn.motionY / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
    }

    /**
     * Determines whether the block can stay on the position based on its surroundings
     */
    abstract fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean

    override fun isReplaceable(world: IBlockAccess, pos: BlockPos): Boolean {
        return false
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return canBlockStay(worldIn, pos, defaultState)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        checkAndDropBlock(worldIn, pos, state)
    }

    protected open fun checkAndDropBlock(worldIn: World, pos: BlockPos?, state: IBlockState?) {
        if (!canBlockStay(worldIn, pos!!, state!!)) {
            val fluidState = FluidloggedUtils.getFluidState(worldIn, pos, state)
            if (!fluidState.isEmpty) {
                dropBlockAsItem(worldIn, pos, state, 0)
                worldIn.setBlockState(pos, fluidState.state, Constants.BlockFlags.DEFAULT)
            } else {
                worldIn.setBlockState(pos, Blocks.AIR.defaultState, Constants.BlockFlags.NO_RERENDER)
            }
        }
    }

    // IGrowable implementation
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean {
        return canGrow(worldIn, pos, state, false)
    }

    override fun canFluidFlow(world: IBlockAccess, pos: BlockPos, here: IBlockState, side: EnumFacing): Boolean {
        return true
    }

    override fun isFluidValid(state: IBlockState, world: World, pos: BlockPos, fluid: Fluid): Boolean {
        return FluidUtil.areFluidsValid(compatibleFluids, fluid)
    }

    override fun onFluidDrain(world: World, pos: BlockPos, here: IBlockState, blockFlags: Int): EnumActionResult {
        world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, getStateId(here))
        dropBlockAsItem(world, pos, here, 0)
        world.setBlockState(pos, Blocks.AIR.defaultState, blockFlags)
        return EnumActionResult.SUCCESS
    }
}