@file:Suppress("OVERRIDE_DEPRECATION")

package teksturepako.greenery.common.block.plant.upland.single

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.GreeneryPlant
import teksturepako.greenery.common.util.MaterialUtil
import teksturepako.greenery.common.util.Utils.applyOffset
import java.util.*

abstract class SingleUplandPlant(val name: String, maxAge: Int) : GreeneryPlant(maxAge)
{
    companion object
    {
        val AABB = arrayOf(
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
            AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
    }

    // -- BLOCK --

    init
    {
        setRegistryName("plant/upland/single/$name")
        translationKey = "${Greenery.MODID}.$name"
        soundType = SoundType.PLANT
        creativeTab = Greenery.creativeTab
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val materials = MaterialUtil.materialsOf(allowedSoils)

        val down = worldIn.getBlockState(pos.down())
        return down.material in materials
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return AABB[getAge(state)].applyOffset(hasOffset, state, source, pos)
    }

    override fun quantityDroppedWithBonus(fortune: Int, random: Random): Int
    {
        return 1 + random.nextInt(fortune * 2 + 1)
    }

    override fun harvestBlock(worldIn: World, player: EntityPlayer, pos: BlockPos, state: IBlockState, te: TileEntity?, stack: ItemStack)
    {
        if (!worldIn.isRemote && stack.item === Items.SHEARS)
        {
            StatList.getBlockStats(this)?.let { player.addStat(it) }
            spawnAsEntity(worldIn, pos, ItemStack(this, 1))
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack)
        }
    }
}