package teksturepako.greenery.common.config.json

import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.emergent.EmergentPlantBase
import teksturepako.greenery.common.block.plant.submerged.kelplike.KelpLikePlantBase
import teksturepako.greenery.common.block.plant.submerged.tall.TallSubmergedPlantBase
import teksturepako.greenery.common.block.plant.upland.tall.TallPlantBase
import teksturepako.greenery.common.config.json.Deserializer.canDoWork
import teksturepako.greenery.common.config.json.PlantDefaults.emergentDir
import teksturepako.greenery.common.config.json.PlantDefaults.submergedKelpLikeDir
import teksturepako.greenery.common.config.json.PlantDefaults.submergedTallDir
import teksturepako.greenery.common.config.json.PlantDefaults.uplandTallDir
import java.io.File

object Parser
{
    private inline fun File.forPlantFiles(action: (File) -> Unit)
    {
        for (file in this.walk()) if (file.canDoWork()) action(file)
    }

    /**
     * Plant Data Initializer
     * Should be called only once!
     */
    fun initPlantData()
    {
        emergentDir.forPlantFiles {
            val data = Deserializer.getData(it)

            Greenery.plants.add(object : EmergentPlantBase(data.name)
            {
                override var worldGenConfig = data.worldGen
                override var compatibleFluids = data.compatibleFluids
                override var hasTintIndex = data.hasTintIndex
                override var isSolid = data.isSolid
                override var isHarmful = data.isHarmful
            })
        }
        submergedKelpLikeDir.forPlantFiles {
            val data = Deserializer.getData(it)

            Greenery.plants.add(object : KelpLikePlantBase(data.name)
            {
                override var worldGenConfig = data.worldGen
                override var compatibleFluids = data.compatibleFluids
                override var hasTintIndex = data.hasTintIndex
                override var isSolid = data.isSolid
                override var isHarmful = data.isHarmful
            })
        }
        submergedTallDir.forPlantFiles {
            val data = Deserializer.getData(it)

            Greenery.plants.add(object : TallSubmergedPlantBase(data.name)
            {
                override var worldGenConfig = data.worldGen
                override var compatibleFluids = data.compatibleFluids
                override var hasTintIndex = data.hasTintIndex
                override var isSolid = data.isSolid
                override var isHarmful = data.isHarmful
            })
        }
        uplandTallDir.forPlantFiles {
            val data = Deserializer.getData(it)

            Greenery.plants.add(object : TallPlantBase(data.name)
            {
                override var worldGenConfig = data.worldGen
                override var drops = data.drops
                override var hasTintIndex = data.hasTintIndex
                override var isSolid = data.isSolid
                override var isHarmful = data.isHarmful
            })
        }
    }

    /**
     * Plant Data Re-loader
     * Can be called at any time.
     */
    fun reloadPlantData()
    {
        Greenery.plants.forEach { plant ->
            when (plant)
            {
                is EmergentPlantBase -> emergentDir.forPlantFiles {
                    val data = Deserializer.getData(it)

                    if (data.name == plant.name)
                    {
                        plant.worldGenConfig = data.worldGen
                        plant.compatibleFluids = data.compatibleFluids
                        plant.isSolid = data.isSolid
                        plant.isHarmful = data.isHarmful
                    }
                }
                is KelpLikePlantBase -> submergedKelpLikeDir.forPlantFiles {
                    val data = Deserializer.getData(it)

                    if (data.name == plant.name)
                    {
                        plant.worldGenConfig = data.worldGen
                        plant.compatibleFluids = data.compatibleFluids
                        plant.isSolid = data.isSolid
                        plant.isHarmful = data.isHarmful
                    }
                }
                is TallSubmergedPlantBase -> submergedTallDir.forPlantFiles {
                    val data = Deserializer.getData(it)

                    if (data.name == plant.name)
                    {
                        plant.worldGenConfig = data.worldGen
                        plant.compatibleFluids = data.compatibleFluids
                        plant.isSolid = data.isSolid
                        plant.isHarmful = data.isHarmful
                    }
                }
                is TallPlantBase -> uplandTallDir.forPlantFiles {
                    val data = Deserializer.getData(it)

                    if (data.name == plant.name)
                    {
                        plant.worldGenConfig = data.worldGen
                        plant.drops = data.drops
                        plant.isSolid = data.isSolid
                        plant.isHarmful = data.isHarmful
                    }
                }
            }
        }
    }

}