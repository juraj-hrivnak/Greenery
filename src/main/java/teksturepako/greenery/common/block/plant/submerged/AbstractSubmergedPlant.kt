package teksturepako.greenery.common.block.plant.submerged

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fluids.Fluid
import teksturepako.greenery.Greenery
import teksturepako.greenery.client.GreenerySoundTypes
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.block.plant.PlantDamageSource.Companion.Prickly
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.FluidUtil
import java.util.*

abstract class AbstractSubmergedPlant(val name: String, maxAge: Int) : GreeneryPlant(maxAge), IFluidloggable
{
    abstract var compatibleFluids: MutableList<String>

    val ALLOWED_SOILS = setOf<Material>(Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK)
    val TOP_AABB = AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9)
    val BOTTOM_AABB = AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 1.00, 0.9)

    init
    {
        this.setRegistryName("plant/submerged/$name")
        this.setTranslationKey("${Greenery.MODID}.$name")
        this.setSoundType(GreenerySoundTypes.SEAWEED)
        this.setCreativeTab(Greenery.creativeTab)
        this.setLightOpacity(0)
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
    fun canGenerateBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        return FluidUtil.canGenerateInFluids(compatibleFluids, worldIn, pos) && canBlockStay(worldIn, pos, defaultState)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random)
    {
        if (getAge(state) <= maxAge && canGenerateBlockAt(worldIn, pos))
        {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((25.0f / 1.0f).toInt() + 1) == 0))
            {
                grow(worldIn, rand, pos, state)
                ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos))
            }
        }
    }

    /**
     * Checks if there is a compatible fluid block on the placing position.
     * Warning: This should not be used in world generation because of performance reasons!
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        val fluidState = FluidloggedUtils.getFluidState(worldIn, pos)
        return (!fluidState.isEmpty && isFluidValid(defaultState, worldIn, pos, fluidState.fluid)
                && FluidloggedUtils.isFluidloggableFluid(fluidState.state, worldIn, pos)
                && canBlockStay(worldIn, pos, defaultState))
    }

    // Submerged plants are not replaceable.
    override fun isReplaceable(worldIn: IBlockAccess, pos: BlockPos): Boolean = false

    // IGrowable implementation
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState): Boolean
    {
        return canGrow(worldIn, pos, state, false)
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