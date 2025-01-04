package teksturepako.greenery.common.config.parser

import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary
import teksturepako.greenery.common.util.Utils.isNotNull

object DropsParser
{
    fun getDrops(
        dropsList: List<String>,
        world: IBlockAccess,
        pos: BlockPos,
        state: IBlockState,
        defaultItem: Item,
        fortune: Int
    ): List<ItemStack>
    {
        val drops: MutableList<ItemStack> = ArrayList()
        val random = (world as World).rand

        for (stringInput in dropsList)
        {
            val filteredInput = stringInput.filter { !it.isWhitespace() }.trim()

            var raw: List<String>

            if (filteredInput.contains("|"))
            {
                raw = filteredInput.split("|")
            }
            else continue

            // Block state check
            var blockStateIsValid = true
            if (raw.isNotNull(2))
            {
                blockStateIsValid = isBlockStateValid(state.getActualState(world, pos), raw[2])
            }

            // Item stack check
            var itemStackRaw: List<String> = emptyList()
            var amount = 1

            if (raw.isNotNull(0))
            {
                itemStackRaw = raw[0].substringBefore("*").split(":")
                raw[0].split("*").run { if (this.isNotNull(1)) amount = this[1].toInt() }
            }

            // Parsing itemStack
            val itemStack: ItemStack = if (itemStackRaw.isNotNull(0) && itemStackRaw.isNotNull(1))
            {
                if (itemStackRaw[0] == "ore")
                {
                    OreDictionary.getOres(itemStackRaw[1]).firstOrNull() ?: ItemStack.EMPTY
                }
                else
                {
                    ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation("${itemStackRaw[0]}:${itemStackRaw[1]}"))!!)
                }
            }
            else
            {
                when
                {
                    "seeds" in raw[0] ->
                    {
                        val seed = ForgeHooks.getGrassSeed(random, fortune)
                        if (!seed.isEmpty) seed else ItemStack.EMPTY
                    }
                    "this" in raw[0] -> ItemStack(defaultItem)
                    else -> ItemStack.EMPTY
                }
            }

            // Setting metadata if it's not an oredict item
            if (itemStackRaw[0] != "ore" && itemStackRaw.isNotNull(2))
            {
                itemStack.itemDamage = itemStackRaw[2].toInt()
            }

            // Fix itemDamage overflow
            // Encountered when using 'ore:plankWood' or 'ore:logWood' as drop
            if (itemStack.itemDamage == 32767) itemStack.itemDamage = 0

            // Setting count
            itemStack.count = amount + fortune

            val chance: Double = if (raw.isNotNull(1)) raw[1].toDouble() else 0.0

            // Adding parsed loot to the list
            if (random.nextDouble() < chance && blockStateIsValid)
            {
                drops.add(itemStack)
            }
        }
        return drops
    }
}
