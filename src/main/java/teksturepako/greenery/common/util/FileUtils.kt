package teksturepako.greenery.common.util

import java.io.File

object FileUtils
{
    infix fun File.subfolder(subFileName: String): File
    {
        this.mkdirs()
        val file = File(this, subFileName)
        file.mkdirs()
        return file
    }
}