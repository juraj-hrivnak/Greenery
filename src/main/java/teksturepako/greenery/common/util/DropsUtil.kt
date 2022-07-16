package teksturepako.greenery.common.util

import net.minecraft.block.state.IBlockState
import net.minecraft.command.CommandBase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.registry.ForgeRegistries

@Suppress("DEPRECATION")
object DropsUtil {
    fun getDrops(
        dropsList: MutableList<String>,
        drops: NonNullList<ItemStack>,
        world: IBlockAccess,
        pos: BlockPos,
        state: IBlockState,
        defaultItem: Item,
        fortune: Int
    ) {
        val random = (world as World).rand
        for (stringInput in dropsList) {
            val filteredInput = stringInput.filter { !it.isWhitespace() }.trim()

            var raw: List<String> = emptyList()
            if (filteredInput.contains("|")) {
                raw = filteredInput.split("|")
            }

            /**
             * Declaring variables
             */
            val actualState = state.getActualState(world, pos)
            val blockStateIsValid: Boolean = if (raw.getOrNull(2) != null) {
                val blockState = CommandBase.convertArgToBlockState(state.block, raw[2])
                actualState == blockState
            } else true

            /**
             * Declaring variables
             */
            var itemStackRaw: List<String> = emptyList()
            if (raw.getOrNull(0) != null) {
                itemStackRaw = raw[0].split(":")
            }

            /**
             * Declaring variables
             */
            val itemStack: ItemStack = if (itemStackRaw.getOrNull(0) != null && itemStackRaw.getOrNull(1) != null) {
                ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation("${itemStackRaw[0]}:${itemStackRaw[1]}"))!!)
            } else {
                if (itemStackRaw.getOrNull(0) != null && itemStackRaw.getOrNull(1) == null) {
                    if (itemStackRaw[0].contains("\$defaultSeeds")) {
                        val seed = ForgeHooks.getGrassSeed(random, fortune)
                        if (!seed.isEmpty) {
                            seed
                        } else ItemStack.EMPTY
                    } else if (filteredInput.contains("\$defaultItem")) {
                        ItemStack(defaultItem)
                    } else ItemStack.EMPTY
                } else {
                    ItemStack.EMPTY
                }
            }

            /**
             * Declaring variables
             */
            val count: Int = if (itemStackRaw.getOrNull(2) != null) itemStackRaw[2].toInt() else 1

            /**
             * Declaring variables
             */
            val chance: Double = if (raw.getOrNull(1) != null) raw[1].toDouble() else 0.0


            /**
             * Adding parsed loot to the list
             */
            if (random.nextDouble() < chance && blockStateIsValid) {
                repeat(count + fortune) {
                    drops.add(itemStack)
                }
            }
        }
    }
}