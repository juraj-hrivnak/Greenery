package teksturepako.greenery.common.command

import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import teksturepako.greenery.Greenery
import teksturepako.greenery.common.config.json.Parser


class CommandGreenery : CommandBase()
{
    override fun getName(): String = "greenery"

    override fun getUsage(sender: ICommandSender): String = "command.greenery.usage"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>)
    {
        if (args.isEmpty())
        {
            throw WrongUsageException("command.greenery.usage", *arrayOfNulls(0))
        }
        else if (args[0] == "reload")
        {
            Parser.reloadPlantData()

            Greenery.generators.clear()
            Greenery.loadGenerators(true)

            sender.sendMessage(TextComponentString("Plant configuration reloaded!").setStyle(Style().setColor(TextFormatting.GREEN)))
        }
    }

    override fun getRequiredPermissionLevel(): Int = 2

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<out String>, targetPos: BlockPos?): MutableList<String>
    {
        return if (args.size == 1)
        {
            getListOfStringsMatchingLastWord(args, "reload")
        }
        else super.getTabCompletions(server, sender, args, targetPos)
    }

}

