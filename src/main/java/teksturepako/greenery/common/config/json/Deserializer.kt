package teksturepako.greenery.common.config.json

import kotlinx.serialization.json.Json
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

object Deserializer
{
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    fun getData(jsonFile: File): PlantData
    {
        val data = json.decodeFromString<PlantData>(readFile(jsonFile.toString()))
        data.name = jsonFile.nameWithoutExtension.toSnakeCase()
        return data
    }

    fun File.canDoWork(): Boolean
    {
        return this.exists() && this.extension == "json"
    }

    private fun readFile(file: String): String
    {
        return FileUtils.readFileToString(File(file), StandardCharsets.UTF_8)
    }

    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    private fun String.toSnakeCase(): String
    {
        return camelRegex.replace(this) { "_${it.value}" }.lowercase()
    }
}
