package teksturepako.greenery.common.config.json.arbBlock

import teksturepako.greenery.Greenery
import kotlin.io.path.Path
import kotlin.io.path.pathString

object ArbBlockParser
{
    private val arbBlocksPath = Path(Greenery.configFolder.pathString, "blocks")

    fun decodeOrReloadData()
    {
        Greenery.arbBlocks.clear()
        arbBlocksPath.decodeArbBlockDataRecursive { Greenery.arbBlocks.add(it) }
    }
}