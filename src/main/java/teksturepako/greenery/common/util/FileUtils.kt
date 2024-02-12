package teksturepako.greenery.common.util

import java.io.File

object FileUtils
{
    operator fun File.div(subFileName: String): File
    {
        this.mkdirs()
        val file = File(this, subFileName)
        file.mkdirs()
        return file
    }
}