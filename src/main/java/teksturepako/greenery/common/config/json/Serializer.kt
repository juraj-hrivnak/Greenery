package teksturepako.greenery.common.config.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.json.Deserializer.getOrCreateSubfolder
import java.io.File
import java.io.FileOutputStream

object Serializer
{
    private val json = Json {
        prettyPrint = true
    }

    private val plantDir = Greenery.configFolder.getOrCreateSubfolder("plants")
    private val emergentDir = plantDir.getOrCreateSubfolder("emergent")
    private val uplandDir = plantDir.getOrCreateSubfolder("upland")
    private val uplandTallDir = uplandDir.getOrCreateSubfolder("tall")

    @OptIn(ExperimentalSerializationApi::class)
    fun initDefaults()
    {
        PlantDefaults.emergentPlants.forEach { data ->
            if (!File(emergentDir, "${data.name}.json").exists())
            {
                FileOutputStream(File(emergentDir, "${data.name}.json")).use {
                    json.encodeToStream<PlantData>(data, it)
                }
            }
        }
        PlantDefaults.uplandTallPlants.forEach { data ->
            if (!File(uplandTallDir, "${data.name}.json").exists())
            {
                FileOutputStream(File(uplandTallDir, "${data.name}.json")).use {
                    json.encodeToStream<PlantData>(data, it)
                }
            }
        }
    }
}