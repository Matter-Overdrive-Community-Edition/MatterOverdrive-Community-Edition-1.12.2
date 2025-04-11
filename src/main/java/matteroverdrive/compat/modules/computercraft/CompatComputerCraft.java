package matteroverdrive.compat.modules.computercraft;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import dan200.computercraft.api.ComputerCraftAPI;
import matteroverdrive.compat.Compat;

/**
 * @author shadowfacts
 */
@Compat("computercraft")
public class CompatComputerCraft
{

	@Compat.Init
	public static void init(FMLInitializationEvent event)
	{
		ComputerCraftAPI.registerPeripheralProvider(new MOPeripheralProvider());
	}

}
