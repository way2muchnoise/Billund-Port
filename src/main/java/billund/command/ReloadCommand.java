package billund.command;

import billund.network.MessageHandler;
import billund.network.message.MessageServerSync;
import billund.registry.BillundSetRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.LinkedList;
import java.util.List;

public class ReloadCommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "billund";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/billund reload";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length != 1)
            throw new CommandException("/billund reload");

        if (args[0].equals("reload"))
        {
            BillundSetRegistry.instance().reload();
            if (!sender.getEntityWorld().isRemote)
                MessageHandler.INSTANCE.sendToAll(new MessageServerSync());
            sender.addChatMessage(new ChatComponentText("\u00A7a" + StatCollector.translateToLocal("billund.command.reloaded")));
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        List<String> list = new LinkedList<String>();
        if (args.length > 1) return list;

        if ("reload".startsWith(args[0]))
            list.add("reload");

        return list;
    }
}
