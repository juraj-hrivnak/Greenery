package teksturepako.greenery.common.util

import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.registry.ForgeRegistries


object DropsUtil
{
    fun getDrops(dropsList: MutableList<String>, world: IBlockAccess, pos: BlockPos, state: IBlockState, defaultItem: Item, fortune: Int): List<ItemStack>
    {
        val drops: MutableList<ItemStack> = ArrayList()
        val random = (world as World).rand

        for (stringInput in dropsList)
        {
            val filteredInput = stringInput.filter { !it.isWhitespace() }.trim()

            var raw: List<String> = emptyList()
            if (filteredInput.contains("|"))
            {
                raw = filteredInput.split("|")
            }

            var blockStateIsValid = true
            if (raw.isNotNull(2))
            {
                blockStateIsValid = isBlockStateValid(state.getActualState(world, pos), raw[2])
            }

            var itemStackRaw: List<String> = emptyList()
            if (raw.isNotNull(0))
            {
                itemStackRaw = raw[0].split(":")
            }

            val itemStack: ItemStack = if (itemStackRaw.isNotNull(0) && itemStackRaw.isNotNull(1))
            {
                ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation("${itemStackRaw[0]}:${itemStackRaw[1]}"))!!)
            }
            else if (itemStackRaw.isNotNull(0) && itemStackRaw.isNotNull(1))
            {
                when
                {
                    itemStackRaw[0].contains("\$defaultSeeds") ->
                    {
                        val seed = ForgeHooks.getGrassSeed(random, fortune)
                        if (!seed.isEmpty)
                        {
                            seed
                        }
                        else ItemStack.EMPTY
                    }
                    filteredInput.contains("\$defaultItem") ->
                    {
                        ItemStack(defaultItem)
                    }
                    else -> ItemStack.EMPTY
                }
            }
            else ItemStack.EMPTY

            if (itemStackRaw.isNotNull(2)) itemStack.count = itemStackRaw[2].toInt() + fortune

            val chance: Double = if (raw.isNotNull(1)) raw[1].toDouble() else 0.0

            // Adding parsed loot to the list
            if (random.nextDouble() < chance && blockStateIsValid)
            {
                drops.add(itemStack)
            }
        }
        return drops
    }

    fun isBlockStateValid(state: IBlockState, input: String): Boolean
    {
        val y = arrayListOf<Boolean>()
        val input2 = input.split(",")
        input2.asSequence().map { it.split("=") }.forEach {
            for ((key, value) in state.properties)
            {
                y += key.name == it[0] && value.toString() == it[1]
            }
        }
        return y.filter { it }.size == input2.size
    }
}
