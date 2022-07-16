package teksturepako.greenery.common.block.tallgrass

import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.util.ModDamageSource

class BlockNettle : AbstractTallGrass(NAME) {

    companion object {
        const val NAME = "nettle"
        const val REGISTRY_NAME = "${Greenery.MODID}:$NAME"

        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)
        val TOP: PropertyBool = PropertyBool.create("top")
        val SINGLE: PropertyBool = PropertyBool.create("single")
    }

    init {
        defaultState = blockState.baseState
            .withProperty(AGE, 0)
            .withProperty(TOP, true)
            .withProperty(SINGLE, false)
    }

    override val dropsList: MutableList<String>
        get() = Config.generation.nettle.drops.toMutableList()

    override fun getAgeProperty(): PropertyInteger {
        return AGE
    }

    override fun getMaxAge(): Int {
        return 3
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, AGE, TOP, SINGLE)
    }

    @Deprecated("")
    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        val hasTheSameBlockBelow = worldIn.getBlockState(pos.down()).block == this
        val hasTheSameBlockAbove = worldIn.getBlockState(pos.up()).block == this

        return when {
            hasTheSameBlockBelow -> state.withProperty(TOP, true).withProperty(SINGLE, false)
            hasTheSameBlockAbove -> state.withProperty(TOP, false).withProperty(SINGLE, false)
            else -> state.withProperty(TOP, true).withProperty(SINGLE, true)
        }
    }

    @Deprecated("")
    @Suppress("DEPRECATION")
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return when (val actualState = getActualState(state, source, pos)) {
            actualState.withProperty(TOP, true) ->
                GRASS_TOP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(state.getOffset(source, pos))
            actualState.withProperty(TOP, false) ->
                GRASS_BOTTOM_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(
                    state.getOffset(
                        source,
                        pos
                    )
                )
            else -> GRASS_TOP_AABB[(state.getValue(this.ageProperty) as Int).toInt()].offset(
                state.getOffset(
                    source,
                    pos
                )
            )
        }
    }

    override fun onEntityCollision(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.motionX = entityIn.motionX / 1.1
        entityIn.motionZ = entityIn.motionZ / 1.1
        if (entityIn is EntityPlayer) {
            if (entityIn.inventory.armorInventory[0] == ItemStack.EMPTY) {
                entityIn.attackEntityFrom(ModDamageSource.NETTLE, 0.5f)
            }
            if (entityIn.inventory.armorInventory[1] == ItemStack.EMPTY) {
                entityIn.attackEntityFrom(ModDamageSource.NETTLE, 0.5f)
            }
        }
    }
}