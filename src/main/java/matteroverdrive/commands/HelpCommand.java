package matteroverdrive.commands;

import matteroverdrive.Reference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends CommandBase {

    @Override
    public String getName() {
        return "matteroverdrive";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "matteroverdrive for a list of commands";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] parameters) throws CommandException {
		commandSender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + Reference.MOD_NAME + "] " + TextFormatting.RESET + "Help"));
		commandSender.sendMessage(new TextComponentString("/mo_gen : World Generation."));
		commandSender.sendMessage(new TextComponentString("/android : Android Managment."));
		commandSender.sendMessage(new TextComponentString("/matterregistry : Matter Managment."));
		commandSender.sendMessage(new TextComponentString("/quest : Quest Managment."));
	}
}