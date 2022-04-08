
package matteroverdrive.commands;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.world.MOImageGen;
import matteroverdrive.world.buildings.WeightedRandomMOWorldGenBuilding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorldGenCommands extends CommandBase {

    @Override
    public String getName() {
        return "mo_gen";
    }

    @Override
    public String getUsage(ICommandSender commandSender) {
        return "mo_gen <command> <structure name> <options>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender commandSender, String[] parameters) throws CommandException {
        EntityPlayer entityPlayer = null;
		entityPlayer = (EntityPlayer) commandSender;
		 if (parameters.length == 0) {
				commandSender.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + Reference.MOD_NAME + "] " + TextFormatting.RESET + "Help"));
				commandSender.sendMessage(new TextComponentString("Format: /mo_gen generate <structure> <force> <player>"));
				commandSender.sendMessage(new TextComponentString("Example: /mo_gen generate cargo_ship f " + ((EntityPlayer) entityPlayer).getName()));
				commandSender.sendMessage(new TextComponentString("Structures: advfusion, android_house, cargo_ship, crashed_ship, fusion, sand_pit_house, underwater_base"));
			  
			} else {
        if (parameters.length >= 4) {
            entityPlayer = getPlayer(server, commandSender, parameters[3]);
        } else if (commandSender instanceof EntityPlayer) {
            entityPlayer = (EntityPlayer) commandSender;
        }
        boolean forceGeneration = false;
        if (parameters.length >= 3) {
            forceGeneration = parameters[2].contains("f");
        }

        if (parameters.length >= 1) {
            if (parameters[0].equalsIgnoreCase("generate")) {
                if (parameters.length >= 2 && entityPlayer != null) {
                    for (WeightedRandomMOWorldGenBuilding entry : MatterOverdrive.MO_WORLD.worldGen.buildings) {
                        if (entry.worldGenBuilding.getName().equalsIgnoreCase(parameters[1])) {
                            MOImageGen.ImageGenWorker worker = MatterOverdrive.MO_WORLD.worldGen.startBuildingGeneration(entry.worldGenBuilding, entityPlayer.getPosition(), entityPlayer.getRNG(), commandSender.getEntityWorld(), null, null, forceGeneration);
                            if (worker != null) {
                                worker.setPlaceNotify(2);
                            }
                        }
                    }
                }
            }
		}
	}
	}
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            commands.add("generate");
        } else if (args.length == 2) {
            for (WeightedRandomMOWorldGenBuilding entry : MatterOverdrive.MO_WORLD.worldGen.buildings) {
                commands.add(entry.worldGenBuilding.getName());
            }
        } else if (args.length == 4) {
            for (Object entityPlayer : sender.getEntityWorld().playerEntities) {
                commands.add(((EntityPlayer) entityPlayer).getName());
            }
        }
        return commands;
    }
}
