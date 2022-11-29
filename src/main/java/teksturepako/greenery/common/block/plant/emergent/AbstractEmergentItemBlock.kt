package teksturepako.greenery.common.block.plant.emergent

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.common.util.BlockSnapshot
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery

abstract class AbstractEmergentItemBlock(name: String, private val blockToUse: Block) : ItemBlock(blockToUse)
{
    companion object
    {
        val ALLOWED_SOILS = setOf<Material>(
            Material.GROUND, Material.SAND, Material.GRASS, Material.CLAY, Material.ROCK
        )
    }

    init
    {
        setRegistryName("plant/emergent/$name")
        translationKey = name
        creativeTab = Greenery.creativeTab
    }

    fun canBlockStay(worldIn: World, pos: BlockPos): Boolean
    {
        val down = worldIn.getBlockState(pos.down())
        val down2 = worldIn.getBlockState(pos.down(2))

        return if (worldIn.isAirBlock(pos) && down.material == Material.WATER)
        {
            down2.material in ALLOWED_SOILS
        }
        else false
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemRenderer(this, 0, registryName.toString())
    }

    @SideOnly(Side.CLIENT)
    fun registerItemColorHandler(event: ColorHandlerEvent.Item)
    {
        Greenery.proxy.registerItemColourHandler(this, event)
    }

    // TODO: Rewrite this
    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack>
    {
        val itemstack = playerIn.getHeldItem(handIn)
        val raytraceresult = rayTrace(worldIn, playerIn, true)
        return if (raytraceresult == null)
        {
            ActionResult(EnumActionResult.PASS, itemstack)
        }
        else
        {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                val blockpos = raytraceresult.blockPos
                if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(
                        blockpos.offset(
                            raytraceresult.sideHit
                        ), raytraceresult.sideHit, itemstack
                    ))
                {
                    return ActionResult(EnumActionResult.FAIL, itemstack)
                }
                val blockpos1 = blockpos.up()

                if (canBlockStay(worldIn, blockpos1))
                {
                    // special case for handling block placement with water lilies
                    val blocksnapshot = BlockSnapshot.getBlockSnapshot(worldIn, blockpos1)

                    if (ForgeEventFactory.onPlayerBlockPlace(
                            playerIn, blocksnapshot, EnumFacing.UP, handIn
                        ).isCanceled)
                    {
                        blocksnapshot.restore(true, false)
                        return ActionResult(EnumActionResult.FAIL, itemstack)
                    }

                    worldIn.setBlockState(blockpos1, blockToUse.defaultState, 3)

                    if (playerIn is EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger(playerIn, blockpos1, itemstack)
                    }

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        itemstack.shrink(1)
                    }

                    playerIn.addStat(StatList.getObjectUseStats(this))
                    worldIn.playSound(
                        playerIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f
                    )
                    return ActionResult(EnumActionResult.SUCCESS, itemstack)
                }
            }
            ActionResult(EnumActionResult.FAIL, itemstack)
        }
    }

}