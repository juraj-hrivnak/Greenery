package teksturepako.greenery.common.block.plant.floating

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.*
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.common.util.BlockSnapshot
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.GreeneryPlant

class FloatingItemBlock(name: String, private val blockToUse: GreeneryPlant) : ItemBlock(blockToUse)
{
    init
    {
        setRegistryName("plant/floating/$name")
        translationKey = "${Greenery.MODID}.$name"
        creativeTab = Greenery.creativeTab
    }

    @SideOnly(Side.CLIENT)
    fun registerItemModel()
    {
        Greenery.proxy.registerItemRenderer(this, 0, registryName.toString())
    }

    @SideOnly(Side.CLIENT)
    fun registerItemColorHandler(event: ColorHandlerEvent.Item)
    {
        Greenery.proxy.registerItemColorHandler(this, event)
    }

    override fun onItemRightClick(world: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack>
    {
        val heldItem = playerIn.getHeldItem(handIn)
        val rayTraceResult = rayTrace(world, playerIn, true) ?: return ActionResult(EnumActionResult.PASS, heldItem)

        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return ActionResult(EnumActionResult.FAIL, heldItem)

        val blockPos = rayTraceResult.blockPos

        val isBlockModifiable = world.isBlockModifiable(playerIn, blockPos)
        val canPlayerEdit = playerIn.canPlayerEdit(blockPos.offset(rayTraceResult.sideHit), rayTraceResult.sideHit, heldItem)

        if (!isBlockModifiable || !canPlayerEdit) return ActionResult(EnumActionResult.FAIL, heldItem)

        val up = blockPos.up()

        if (!blockToUse.canBlockStay(world, up, world.getBlockState(up))) return ActionResult(EnumActionResult.FAIL, heldItem)

        val blockSnapshot = BlockSnapshot.getBlockSnapshot(world, up)

        if (ForgeEventFactory.onPlayerBlockPlace(playerIn, blockSnapshot, EnumFacing.UP, handIn).isCanceled)
        {
            blockSnapshot.restore(true, false)
            return ActionResult(EnumActionResult.FAIL, heldItem)
        }

        world.setBlockState(up, blockToUse.defaultState, 8)

        if (playerIn is EntityPlayerMP)
        {
            CriteriaTriggers.PLACED_BLOCK.trigger(playerIn, up, heldItem)
        }

        if (!playerIn.capabilities.isCreativeMode)
        {
            heldItem.shrink(1)
        }

        StatList.getObjectUseStats(this)?.let { playerIn.addStat(it) }
        world.playSound(playerIn, blockPos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f)
        return ActionResult(EnumActionResult.SUCCESS, heldItem)
    }
}