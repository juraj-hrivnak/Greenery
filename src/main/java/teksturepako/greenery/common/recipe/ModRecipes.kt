package teksturepako.greenery.common.recipe

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import teksturepako.greenery.Greenery.MODID
import teksturepako.greenery.common.registry.ModItems

object ModRecipes
{
    fun register()
    {
        registerSmelting()
    }

    private fun registerSmelting()
    {
        "plant/submerged/kelp".smeltTo(ModItems.itemDriedKelp)
    }

    /**
     * Registers a smelting recipe for an item if it's not null.
     */
    private fun String.smeltTo(itemStack: Item)
    {
        ForgeRegistries.ITEMS.getValue(ResourceLocation("$MODID:$this"))?.let {
            GameRegistry.addSmelting(it, ItemStack(itemStack), 0.1f)
        }
    }
}