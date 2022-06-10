
package matteroverdrive.handler;

import org.apache.logging.log4j.Level;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.IMOTickable;
import matteroverdrive.util.MOLog;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class TickHandler {
	private final MatterNetworkTickHandler matterNetworkTickHandler;
	private final PlayerEventHandler playerEventHandler;
	private boolean worldStartFired = false;
	private long lastTickTime;
	private int lastTickLength;

	public TickHandler(ConfigurationHandler configurationHandler, PlayerEventHandler playerEventHandler) {
		this.playerEventHandler = playerEventHandler;
		this.matterNetworkTickHandler = new MatterNetworkTickHandler();
		configurationHandler.subscribe(matterNetworkTickHandler);
	}

	// Called when the client ticks.
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) {
			return;
		}

		if (ClientProxy.instance().getClientWeaponHandler() != null) {
			ClientProxy.instance().getClientWeaponHandler().onClientTick(event);
		}

		if (!Minecraft.getMinecraft().isGamePaused() && event.phase.equals(TickEvent.Phase.START)) {
			ClientProxy.questHud.onTick();
		}
	}

	// Called when the server ticks. Usually 20 ticks a second.
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		playerEventHandler.onServerTick(event);

		lastTickLength = (int) (System.nanoTime() - lastTickTime);
		lastTickTime = System.nanoTime();
	}

	public void onServerStart(FMLServerStartedEvent event) {

	}

	// Called when a new frame is displayed (See fps)
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		ClientProxy.instance().getClientWeaponHandler().onTick(event);
	}

	// Called when the world ticks
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (!worldStartFired) {
			onWorldStart(event.side, event.world);
			worldStartFired = true;
		}

		if (event.side.isServer()) {
			FMLCommonHandler.instance().getMinecraftServerInstance().profiler.startSection("MO WorldTick Tiles");
			matterNetworkTickHandler.onWorldTickPre(event.phase, event.world);
			int tileEntityListSize = event.world.loadedTileEntityList.size();

			for (int i = 0; i < tileEntityListSize; i++) {
				try {
					TileEntity tileEntity = event.world.loadedTileEntityList.get(i);
					if (tileEntity instanceof IMOTickable) {
						if (tileEntity instanceof IMatterNetworkHandler) {
							matterNetworkTickHandler.updateHandler((IMatterNetworkHandler) tileEntity, event.phase,
									event.world);
						} else {
							((IMOTickable) tileEntity).onServerTick(event.phase, event.world);
						}

					}
				} catch (Throwable e) {
					MOLog.log(Level.ERROR, e, "There was an Error while updating Matter Overdrive Tile Entities.");
					return;
				}
			}
			FMLCommonHandler.instance().getMinecraftServerInstance().profiler.endSection();

			matterNetworkTickHandler.onWorldTickPost(event.phase, event.world);
		}

		MatterOverdrive.MO_WORLD.onWorldTick(event);
	}

	public void onWorldStart(Side side, World world) {

	}

	public int getLastTickLength() {
		return lastTickLength;
	}
}
