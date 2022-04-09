package teksturepako.greenery.common.registry

import net.minecraftforge.fml.common.registry.GameRegistry
import teksturepako.greenery.ModConfig
import teksturepako.greenery.common.block.flower.AbstractFlower
import teksturepako.greenery.common.world.crop.WorldGenArrowhead
import teksturepako.greenery.common.world.crop.WorldGenCattail
import teksturepako.greenery.common.world.flowers.WorldGenNetherFlowers
import teksturepako.greenery.common.world.flowers.WorldGenOverworldFlowers
import teksturepako.greenery.common.world.grass.WorldGenFerns
import teksturepako.greenery.common.world.grass.WorldGenTallGrass
import teksturepako.greenery.common.world.plant.WorldGenKelp
import teksturepako.greenery.common.world.plant.WorldGenRivergrass
import teksturepako.greenery.common.world.plant.WorldGenSeagrass

object ModWorldgen {
    private fun registerOverworldFlowerGen(flower: AbstractFlower) {
        GameRegistry.registerWorldGenerator(WorldGenOverworldFlowers(flower), 0)
    }

    private fun registerNetherFlowerGen(flower: AbstractFlower) {
        GameRegistry.registerWorldGenerator(WorldGenNetherFlowers(flower), 0)
    }

    fun register() {
        GameRegistry.registerWorldGenerator(WorldGenCattail(), 0)
        GameRegistry.registerWorldGenerator(WorldGenArrowhead(), 0)

        if (ModConfig.Seagrass.enabled) GameRegistry.registerWorldGenerator(WorldGenSeagrass(), 0)

        GameRegistry.registerWorldGenerator(WorldGenRivergrass(), 0)
        GameRegistry.registerWorldGenerator(WorldGenTallGrass(), 0)
        GameRegistry.registerWorldGenerator(WorldGenFerns(), 0)

        if (ModConfig.Kelp.enabled) GameRegistry.registerWorldGenerator(WorldGenKelp(), 0)

        if (ModConfig.Cornflower.enabled) registerOverworldFlowerGen(ModBlocks.blockCornflower)
        if (ModConfig.LilyOfTheValley.enabled) registerOverworldFlowerGen(ModBlocks.blockLilyOfTheValley)
        if (ModConfig.WitherRose.enabled) registerNetherFlowerGen(ModBlocks.blockWitherRose)
    }
}