package teksturepako.greenery.common.config.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import teksturepako.greenery.common.config.Config
import teksturepako.greenery.common.config.json.PlantDefaults.emergentDir
import teksturepako.greenery.common.config.json.PlantDefaults.emergentPlants
import teksturepako.greenery.common.config.json.PlantDefaults.submergedKelpLikeDir
import teksturepako.greenery.common.config.json.PlantDefaults.submergedKelpLikePlants
import teksturepako.greenery.common.config.json.PlantDefaults.submergedTallDir
import teksturepako.greenery.common.config.json.PlantDefaults.submergedTallPlants
import teksturepako.greenery.common.config.json.PlantDefaults.uplandTallDir
import teksturepako.greenery.common.config.json.PlantDefaults.uplandTallPlants
import java.io.File
import java.io.FileOutputStream

object Serializer
{
    private val json = Json {
        prettyPrint = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun initDefaults()
    {
        if (!Config.global.genDefaults) return

        mapOf(
            emergentPlants to emergentDir,
            submergedKelpLikePlants to submergedKelpLikeDir,
            submergedTallPlants to submergedTallDir,
            uplandTallPlants to uplandTallDir
        ).forEach { (plants, dir) ->
            plants.forEach { plantData ->
                val file = File(dir, "${plantData.name}.json")
                if (!file.exists())
                {
                    FileOutputStream(file).use {
                        json.encodeToStream(plantData, it)
                    }
                }
            }
        }
    }
}