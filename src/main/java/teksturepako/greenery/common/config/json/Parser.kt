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
    private inline fun File.getPlantDataFromFiles(action: (PlantData) -> Unit)
    {
        for (file in this.walk()) if (file.canDoWork())
        {
            action(Deserializer.getData(file))
        }
    }

    /**
     * Plant Data Initializer
     * Should be called only once!
     */
    fun initPlantData()
    {
        emergentDir.getPlantDataFromFiles {
            Greenery.plants.add(object : EmergentPlantBase(it.name)
            {
                override var worldGen = it.worldGen
                override var compatibleFluids = it.compatibleFluids
                override var hasTintIndex = it.hasTintIndex
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
        submergedKelpLikeDir.getPlantDataFromFiles {
            Greenery.plants.add(object : KelpLikePlantBase(it.name)
            {
                override var worldGen = it.worldGen
                override var compatibleFluids = it.compatibleFluids
                override var hasTintIndex = it.hasTintIndex
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
        submergedTallDir.getPlantDataFromFiles {
            Greenery.plants.add(object : TallSubmergedPlantBase(it.name)
            {
                override var worldGen = it.worldGen
                override var compatibleFluids = it.compatibleFluids
                override var hasTintIndex = it.hasTintIndex
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
        uplandTallDir.getPlantDataFromFiles {
            Greenery.plants.add(object : TallPlantBase(it.name)
            {
                override var worldGen = it.worldGen
                override var drops = it.drops
                override var hasTintIndex = it.hasTintIndex
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
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
                is EmergentPlantBase -> emergentDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.compatibleFluids = it.compatibleFluids
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }
                is KelpLikePlantBase -> submergedKelpLikeDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.compatibleFluids = it.compatibleFluids
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }
                is TallSubmergedPlantBase -> submergedTallDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.compatibleFluids = it.compatibleFluids
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }
                is TallPlantBase -> uplandTallDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.drops = it.drops
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }
            }
        }
    }

}