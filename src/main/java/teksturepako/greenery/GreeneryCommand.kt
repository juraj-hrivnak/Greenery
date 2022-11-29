package teksturepako.greenery

import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos


class GreeneryCommand : CommandBase()
{
    override fun getName(): String
    {
        return "greenery"
    }

    override fun getUsage(sender: ICommandSender): String
    {
        return "commands.greenery.usage"
    }

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>)
    {
        if (args.isEmpty())
        {
            throw WrongUsageException("commands.greenery.usage", *arrayOfNulls(0))
        }
        else if (args[0] == "reloadGenerators")
        {
            Greenery.generators.clear()
            Greenery.loadGenerators(true)
        }
    }

    override fun getRequiredPermissionLevel(): Int
    {
        return 2
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<out String>, targetPos: BlockPos?): MutableList<String>
    {
        return if (args.size == 1)
        {
            getListOfStringsMatchingLastWord(args, "reloadGenerators")
        }
        else super.getTabCompletions(server, sender, args, targetPos)
    }

}

