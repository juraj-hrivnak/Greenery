package teksturepako.greenery.common.config.json

import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.emergent.EmergentPlant
import teksturepako.greenery.common.block.plant.submerged.kelplike.KelpLikeSubmergedPlant
import teksturepako.greenery.common.block.plant.submerged.tall.TallSubmergedPlant
import teksturepako.greenery.common.block.plant.upland.tall.TallUplandPlant
import teksturepako.greenery.common.config.json.Deserializer.canDoWork
import teksturepako.greenery.common.config.json.PlantDefaults.emergentDir
import teksturepako.greenery.common.config.json.PlantDefaults.submergedKelpLikeDir
import teksturepako.greenery.common.config.json.PlantDefaults.submergedTallDir
import teksturepako.greenery.common.config.json.PlantDefaults.uplandTallDir
import java.io.File

object Parser
{
    private var initialized = false

    private inline fun File.getPlantDataFromFiles(action: (PlantData) -> Unit)
    {
        for (file in this.walk()) if (file.canDoWork())
        {
            action(Deserializer.getData(file))
        }
    }

    private val defMaterials = listOf("ground", "sand", "grass", "clay", "rock")
    private val defGrassMaterials = listOf("grass")

    /**
     * Reads JSON configs and initializes plant data.
     */
    fun initPlantData()
    {
        if (initialized) return else initialized = true

        emergentDir.getPlantDataFromFiles {
            Greenery.plants.add(object : EmergentPlant(it.name, it.maxAge ?: 3)
            {
                override var worldGen = it.worldGen
                override var drops = it.drops
                override var allowedSoils = it.allowedSoils ?: defMaterials
                override var compatibleFluids = it.compatibleFluids
                override var canGrow = it.canGrow
                override var hasTintIndex = it.hasTintIndex
                override var hasOffset = it.hasOffset
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
        submergedKelpLikeDir.getPlantDataFromFiles {
            Greenery.plants.add(object : KelpLikeSubmergedPlant(it.name, it.maxAge ?: 15)
            {
                override var worldGen = it.worldGen
                override var drops = it.drops
                override var allowedSoils = it.allowedSoils ?: defMaterials
                override var compatibleFluids = it.compatibleFluids
                override var canGrow = it.canGrow
                override var hasTintIndex = it.hasTintIndex
                override var hasOffset = it.hasOffset
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
        submergedTallDir.getPlantDataFromFiles {
            Greenery.plants.add(object : TallSubmergedPlant(it.name, it.maxAge ?: 1)
            {
                override var worldGen = it.worldGen
                override var drops = it.drops
                override var allowedSoils = it.allowedSoils ?: defMaterials
                override var compatibleFluids = it.compatibleFluids
                override var canGrow = it.canGrow
                override var hasTintIndex = it.hasTintIndex
                override var hasOffset = it.hasOffset
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
        uplandTallDir.getPlantDataFromFiles {
            Greenery.plants.add(object : TallUplandPlant(it.name, it.maxAge ?: 3)
            {
                override var worldGen = it.worldGen
                override var drops = it.drops
                override var allowedSoils = it.allowedSoils ?: defGrassMaterials
                override var canGrow = it.canGrow
                override var hasTintIndex = it.hasTintIndex
                override var hasOffset = it.hasOffset
                override var isSolid = it.isSolid
                override var isHarmful = it.isHarmful
            })
        }
    }

    /**
     * Reloads plant data.
     */
    fun reloadPlantData()
    {
        for (plant in Greenery.plants)
        {
            when (plant)
            {
                is EmergentPlant -> emergentDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.drops = it.drops
                        plant.allowedSoils = it.allowedSoils ?: defMaterials
                        plant.compatibleFluids = it.compatibleFluids
                        plant.canGrow = it.canGrow
                        plant.hasOffset = it.hasOffset
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }

                is KelpLikeSubmergedPlant -> submergedKelpLikeDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.drops = it.drops
                        plant.allowedSoils = it.allowedSoils ?: defMaterials
                        plant.compatibleFluids = it.compatibleFluids
                        plant.canGrow = it.canGrow
                        plant.hasOffset = it.hasOffset
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }

                is TallSubmergedPlant -> submergedTallDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.drops = it.drops
                        plant.allowedSoils = it.allowedSoils ?: defMaterials
                        plant.compatibleFluids = it.compatibleFluids
                        plant.canGrow = it.canGrow
                        plant.hasOffset = it.hasOffset
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }

                is TallUplandPlant -> uplandTallDir.getPlantDataFromFiles {
                    if (it.name == plant.name)
                    {
                        plant.worldGen = it.worldGen
                        plant.drops = it.drops
                        plant.allowedSoils = it.allowedSoils ?: defGrassMaterials
                        plant.canGrow = it.canGrow
                        plant.hasOffset = it.hasOffset
                        plant.isSolid = it.isSolid
                        plant.isHarmful = it.isHarmful
                    }
                }
            }
        }
    }

}