package teksturepako.greenery.common.block.plant.upland.single

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.GreeneryPlantBase
import teksturepako.greenery.common.util.DropsUtil
import java.util.*

abstract class AbstractSinglePlant(name: String) : GreeneryPlantBase()
{

    companion object
    {
        val ALLOWED_SOILS = setOf<Material>(Material.GRASS)
        val AABB = arrayOf(
                AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.50, 0.9),
                AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.625, 0.9),
                AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.75, 0.9),
                AxisAlignedBB(0.10, 0.025, 0.10, 0.9, 0.875, 0.9)
        )
    }

    abstract val drops: MutableList<String>

    init
    {
        setRegistryName("plant/upland/single/$name")
        translationKey = name
        soundType = SoundType.PLANT
        creativeTab = Greenery.creativeTab
    }

    override fun canBlockStay(worldIn: World, pos: BlockPos, state: IBlockState): Boolean
    {
        val down = worldIn.getBlockState(pos.down())
        return down.material in ALLOWED_SOILS
    }

    @Deprecated("Deprecated in Java", ReplaceWith("false"))
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(state.getOffset(source, pos))
    }

    // Drops
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int)
    {
        DropsUtil.getDrops(this.drops, drops, world, pos, state, this.seed, fortune)
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