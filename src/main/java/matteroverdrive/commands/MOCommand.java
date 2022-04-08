package matteroverdrive.commands;

import matteroverdrive.Reference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MOCommand extends CommandBase {
    private final String name;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public MOCommand(String name) {
        this.name = name;
    }

    public void addCommand(SubCommand command) {
        subCommands.add(command);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/" + getName() + " <subcommand>";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] args) throws CommandException {
        if (args.length == 0) {
				iCommandSender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + Reference.MOD_NAME + "] " + TextFormatting.RESET + "Help"));
				iCommandSender.sendMessage(new TextComponentString("Format: /matterregistry <recalculate/blacklist/register> <item/itemstack/ore> <matter value> <player(optional)>"));
				iCommandSender.sendMessage(new TextComponentString("Example: /matterregistry register 3000 "));
				iCommandSender.sendMessage(new TextComponentString("Will register the item you are holding with a matter value of 3000, Specifing a Player name will only set a value for that player."));
				return;
		}
        for (SubCommand command : subCommands) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                command.execute(minecraftServer, iCommandSender, args);
                return;
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (SubCommand command : subCommands) {
                if (command.getName().startsWith(args[0].toLowerCase()))
                    completions.add(command.getName());
            }
        }
        return completions;
    }
}