package teksturepako.greenery.common.config.json.plant

import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.emergent.EmergentPlant
import teksturepako.greenery.common.block.plant.floating.FloatingPlant
import teksturepako.greenery.common.block.plant.submerged.kelplike.KelpLikeSubmergedPlant
import teksturepako.greenery.common.block.plant.submerged.tall.TallSubmergedPlant
import teksturepako.greenery.common.block.plant.upland.tall.TallUplandPlant

object PlantParser
{
    private var initialized = false

    val defMaterials = listOf("ground", "sand", "grass", "clay", "rock")
    val defGrassMaterials = listOf("grass")

    fun initDefaults()
    {
        Emergent.encodeDefaults()
        Floating.encodeDefaults()
        Submerged.KelpLike.encodeDefaults()
        Submerged.Tall.encodeDefaults()
        Upland.Tall.encodeDefaults()
    }

    /**
     * Reads JSON configs and initializes plant data.
     */
    fun decodeData()
    {
        if (initialized) return else initialized = true

        Emergent.path.decodePlantDataRecursive {
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
        Floating.path.decodePlantDataRecursive {
            Greenery.plants.add(object : FloatingPlant(it.name, it.maxAge ?: 15)
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
        Submerged.KelpLike.path.decodePlantDataRecursive {
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
        Submerged.Tall.path.decodePlantDataRecursive {
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
        Upland.Tall.path.decodePlantDataRecursive {
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
                is EmergentPlant -> Emergent.path.decodePlantDataRecursive {
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

                is FloatingPlant -> Floating.path.decodePlantDataRecursive {
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

                is KelpLikeSubmergedPlant -> Submerged.KelpLike.path.decodePlantDataRecursive {
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

                is TallSubmergedPlant -> Submerged.Tall.path.decodePlantDataRecursive {
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

                is TallUplandPlant -> Upland.Tall.path.decodePlantDataRecursive {
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