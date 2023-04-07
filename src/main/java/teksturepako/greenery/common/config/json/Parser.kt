package teksturepako.greenery.common.config.json

import teksturepako.greenery.Greenery
import teksturepako.greenery.common.block.plant.emergent.EmergentPlantBase
import teksturepako.greenery.common.block.plant.upland.tall.TallPlantBase
import teksturepako.greenery.common.config.json.Deserializer.canDoWork
import teksturepako.greenery.common.config.json.Deserializer.getOrCreateSubfolder
import java.io.File

object Parser
{
    private val plantDir = Greenery.configFolder.getOrCreateSubfolder("plants")
    private val emergentDir = plantDir.getOrCreateSubfolder("emergent")
    private val uplandDir = plantDir.getOrCreateSubfolder("upland")
    private val uplandTallDir = uplandDir.getOrCreateSubfolder("tall")

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