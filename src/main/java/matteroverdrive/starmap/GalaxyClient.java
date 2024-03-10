
package matteroverdrive.starmap;

import matteroverdrive.api.starmap.IShip;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GalaxyClient extends GalaxyCommon {
	// region Private Vars
	private static GalaxyClient instance;
	// endregion

	// region Constructors
	public GalaxyClient() {
		super();
	}
	// endregion

    public boolean canSeePlanetInfo(Planet planet,EntityPlayer player)
    {
        if (planet.isOwner(player) || player.capabilities.isCreativeMode)
        {
            return true;
        }

        for (ItemStack shipStack : planet.getFleet())
        {
            if (((IShip)shipStack.getItem()).isOwner(shipStack,player))
            {
                return true;
            }
        }

        return false;
    }

	public boolean canSeeStarInfo(Star star, EntityPlayer player) {
		for (Planet planet : star.getPlanets()) {
			if (canSeePlanetInfo(planet, player)) {
				return true;
			}
		}
		return false;
	}

	// region Events
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getMinecraft().world != null && theGalaxy != null && !Minecraft.getMinecraft().isGamePaused()
				&& Minecraft.getMinecraft().world.isRemote
				&& Minecraft.getMinecraft().world.provider.getDimension() == 0 && event.phase == TickEvent.Phase.START
				&& Minecraft.getMinecraft().world != null) {
			theGalaxy.update(Minecraft.getMinecraft().world);
		}
	}
    //endregion

	// region Getters and Setters
	public static GalaxyClient getInstance() {
		if (instance == null) {
			instance = new GalaxyClient();
		}

		return instance;
	}
	// endregion
}
