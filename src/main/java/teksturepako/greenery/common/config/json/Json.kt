package teksturepako.greenery.common.config.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.extension

@OptIn(ExperimentalSerializationApi::class)
@Suppress("ObjectPropertyName")
val _json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    prettyPrint = true
    allowTrailingComma = true
}

fun Path.canDecode(): Boolean
{
    return this.exists() && this.extension == "json"
}

private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

fun String.toSnakeCase(): String
{
    return camelRegex.replace(this) { "_${it.value}" }.lowercase()
}