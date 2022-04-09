package teksturepako.greenery.common.recipe

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.registry.ModBlocks
import teksturepako.greenery.common.registry.ModItems

object ModRecipes {
    fun register() {
        registerSmelting()
    }

    private fun registerSmelting() {
        if (ModConfig.Kelp.enabled && ModConfig.Kelp.driedKelpEnabled) {
            GameRegistry.addSmelting(
                ModBlocks.blockKelp.itemBlock,
                ItemStack(ModItems.itemDriedKelp),
                0.1f
            )
        }
    }
}