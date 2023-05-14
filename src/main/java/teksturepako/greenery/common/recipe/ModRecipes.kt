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
        "plant/submerged/kelp".maybeSmeltTo(ModItems.itemDriedKelp)
    }

    private fun String.maybeSmeltTo(itemStack: Item)
    {
        if (ForgeRegistries.ITEMS.getValue(ResourceLocation("$MODID:$this")) != null)
        {
            GameRegistry.addSmelting(
                ForgeRegistries.ITEMS.getValue(ResourceLocation("$MODID:$this")),
                ItemStack(itemStack), 0.1f
            )
        }
    }
}