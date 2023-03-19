@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.emergent

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
import teksturepako.greenery.common.config.Config
import java.util.*

abstract class AbstractEmergentPlant(val name: String) : GreeneryPlant()
{
    companion object
    {
        val ALLOWED_SOILS = setOf<Material>(Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK)
        val WATER_CROP_AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
    }

    abstract val compatibleFluids: MutableList<String>

    init
    {
        setRegistryName("plant/emergent/$name")
        translationKey = name
        soundType = GreenerySoundTypes.SEAWEED
        creativeTab = Greenery.creativeTab
    }

    /**
     * Creates an Item Block
     */
    override fun createItemBlock(): Item
    {
        itemBlock = EmergentItemBlock(name, this)
        return itemBlock
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.motionX = entityIn.motionX / (Config.global.slowdownModifier + 1)
        entityIn.motionY = entityIn.motionY / (Config.global.slowdownModifier + 1)
        entityIn.motionZ = entityIn.motionZ / (Config.global.slowdownModifier + 1)
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

    override fun placePlant(world: World, pos: BlockPos, rand: Random, flags: Int)
    {
        if (world.isAirBlock(pos))
        {
            val startingAge = rand.nextInt(this.maxAge)
            val state = this.defaultState.withProperty(this.ageProperty, startingAge)

            if (this.canBlockStay(world, pos, state))
            {
                world.setBlockState(pos, state, flags)
            }
        }
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return WATER_CROP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(state.getOffset(source, pos))
    }
}