package teksturepako.greenery.common.util

import net.minecraft.block.material.Material

object MaterialUtil
{
    fun materialsOf(materials: Collection<String>) = materials.mapNotNull { material ->
        when (material.uppercase())
        {
            "AIR" -> Material.AIR
            "GRASS" -> Material.GRASS
            "GROUND" -> Material.GROUND
            "WOOD" -> Material.WOOD
            "ROCK" -> Material.ROCK
            "IRON" -> Material.IRON
            "ANVIL" -> Material.ANVIL
            "WATER" -> Material.WATER
            "LAVA" -> Material.LAVA
            "LEAVES" -> Material.LEAVES
            "PLANTS" -> Material.PLANTS
            "VINE" -> Material.VINE
            "SPONGE" -> Material.SPONGE
            "CLOTH" -> Material.CLOTH
            "FIRE" -> Material.FIRE
            "SAND" -> Material.SAND
            "CIRCUITS" -> Material.CIRCUITS
            "CARPET" -> Material.CARPET
            "GLASS" -> Material.GLASS
            "REDSTONE_LIGHT" -> Material.REDSTONE_LIGHT
            "TNT" -> Material.TNT
            "CORAL" -> Material.CORAL
            "ICE" -> Material.ICE
            "PACKED_ICE" -> Material.PACKED_ICE
            "SNOW" -> Material.SNOW
            "CRAFTED_SNOW" -> Material.CRAFTED_SNOW
            "CACTUS" -> Material.CACTUS
            "CLAY" -> Material.CLAY
            "GOURD" -> Material.GOURD
            "DRAGON_EGG" -> Material.DRAGON_EGG
            "PORTAL" -> Material.PORTAL
            "CAKE" -> Material.CAKE
            "WEB" -> Material.WEB
            "PISTON" -> Material.PISTON
            "BARRIER" -> Material.BARRIER
            "STRUCTURE_VOID" -> Material.STRUCTURE_VOID
            else -> null
        }
    }.toSet()

    fun materialsOf(vararg materials: String): Set<Material> = materialsOf(materials.toList())
}