
package matteroverdrive.starmap;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.network.packet.client.starmap.PacketUpdateGalaxy;
import matteroverdrive.network.packet.client.starmap.PacketUpdatePlanet;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.starmap.data.TravelEvent;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GalaxyServer extends GalaxyCommon implements IConfigSubscriber {
	// region Static Vars
	public static final int GALAXY_VERSION = 0;
	// endregion
	// region Private Vars
	private static GalaxyServer instance;
	private final GalaxyGenerator galaxyGenerator;
	// endregion

	// region Constructors
	public GalaxyServer() {
		super();
		galaxyGenerator = new GalaxyGenerator();
	}
	// endregion

	public static GalaxyServer getInstance() {
		if (instance == null) {
			instance = new GalaxyServer();
		}
		return instance;
	}
	

	// region Saving and Creation
	public void createGalaxy(File file, World world) {
		theGalaxy = galaxyGenerator.generateGalaxy("Galaxy", world.provider.getDimension(),
				world.getWorldInfo().getSeed(), world);
		saveGalaxy(file);
	}
	// endregion

	public boolean saveGalaxy(File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			NBTTagCompound tagCompound = new NBTTagCompound();
			theGalaxy.writeToNBT(tagCompound);
			CompressedStreamTools.writeCompressed(tagCompound, fileOutputStream);
			fileOutputStream.close();
			return true;
		} catch (IOException e) {
			MOLog.error("Galaxy could not be saved", e);
			return false;
		}
	}
	// endregion

	// region Loading
	public boolean loadGalaxy(File file, World world) {
		if (file.exists() && file.isFile()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				NBTTagCompound tagCompound = CompressedStreamTools.readCompressed(inputStream);
				inputStream.close();
				Galaxy theGalaxy = new Galaxy(world);
				theGalaxy.readFromNBT(tagCompound, galaxyGenerator);
				if (theGalaxy.getVersion() < GALAXY_VERSION) {
					MOLog.info("Galaxy Version is too old. Galaxy Needs regeneration");
					galaxyGenerator.regenerateQuadrants(theGalaxy);
					theGalaxy.setVersion(GALAXY_VERSION);
					theGalaxy.markDirty();
				}
				setTheGalaxy(theGalaxy);
				return true;
			} catch (IOException e) {
				MOLog.error("Could not load galaxy from file", e);
				return false;
			}
		} else {
			MOLog.info("Galaxy File could not be found at: '%s'", file);
		}
		return false;
	}

	/**
	 * Claims a planet as the given players homeworld
	 *
	 * @param player the player that claims the planet
	 * @return The claimed planet. Null if none were claimed.
	 */
	private Planet claimPlanet(EntityPlayer player) {
		UUID playerUUID = EntityPlayer.getUUID(player.getGameProfile());
		int quadrantID = random.nextInt(theGalaxy.getQuadrants().size());
		for (Quadrant quadrant : theGalaxy.getQuadrants()) {
			if (quadrant.getId() == quadrantID) {
				for (Star star : quadrant.getStars()) {
					if (star.getPlanets().size() > 0) {
						boolean isClaimed = false;
						for (Planet planet : star.getPlanets()) {
							if (planet.hasOwner() && !planet.getOwnerUUID().equals(playerUUID)) {
								isClaimed = true;
								break;
							}
						}

						if (!isClaimed) {
							int planetID = random.nextInt(star.getPlanets().size());
							Planet planet = (Planet) star.getPlanets().toArray()[planetID];
							buildHomeworld(planet, player);
							return planet;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Modifyes the planet to be a homeworld, by adding more fleet and building
	 * spaces and building the starting buildings, such as a Base and a Ship Factory
	 *
	 * @param planet The planet that will be transformed into a homeworld
	 * @param player The player that will own the homeworld
	 */
	private void buildHomeworld(Planet planet, EntityPlayer player) {
		planet.setOwner(player);
		planet.setHomeworld(true);
        planet.setBuildingSpaces(8);
        planet.setFleetSpaces(10);
        ItemStack base = new ItemStack(MatterOverdrive.ITEMS.buildingBase);
        ((IBuilding)base.getItem()).setOwner(base,EntityPlayer.getUUID(player.getGameProfile()));
        planet.addBuilding(base);
        ItemStack scoutShip = new ItemStack(MatterOverdrive.ITEMS.scoutShip);
        ((IShip)scoutShip.getItem()).setOwner(scoutShip,EntityPlayer.getUUID(player.getGameProfile()));
        planet.addShip(scoutShip);
		planet.markDirty();
	}

	/**
	 * This function checks if a player already has a homeworld and if not, then
	 * claims a random world and puts it in the homePlanets list
	 *
	 * @param player The player that wants to claim a planet as their homeworld
	 * @return if the player already has a world, returns false, if claiming was
	 *         successful, returns true
	 */
	private boolean tryAndClaimPlanet(EntityPlayer player) {
		UUID playerUUID = EntityPlayer.getUUID(player.getGameProfile());
		if (!homePlanets.containsKey(playerUUID)) {
			Planet claimedPlanet = claimPlanet(player);
			if (claimedPlanet != null) {
				homePlanets.put(playerUUID, claimedPlanet);
				return true;
			} else {
				MOLog.warn("%s could not claim planet.", player.getDisplayName().getFormattedText());
			}
		}
		return false;
	}

    /**
     * a Helper function that tries to create a travel event from one
     * Galactic Position to another, by checking if the event can be finished
     * and if so, then removes the ship from the source planet and puts it in the
     * travel event and returns the event itself
     * @param from The source planet position
     * @param to The Destination Planet position
     * @param shipID The Id of the ship. The id will be checked if valid
     * @return The Travel event if valid, and if not then returns null
     */
    public TravelEvent createTravelEvent(GalacticPosition from,GalacticPosition to,int shipID)
    {
        Planet planet = theGalaxy.getPlanet(from);
        if (planet != null) {
            ItemStack ship = planet.getShip(shipID);
            if (ship != null) {
                TravelEvent travelEvent = new TravelEvent(world, from, to, ship, GalaxyServer.getInstance().getTheGalaxy());
                if (travelEvent.isValid(GalaxyServer.getInstance().getTheGalaxy())) {
                    if (GalaxyServer.getInstance().getTheGalaxy().canCompleteTravelEvent(travelEvent)) {
                        theGalaxy.getPlanet(from).removeShip(shipID);
                        theGalaxy.getPlanet(from).markDirty();
                        GalaxyServer.getInstance().getTheGalaxy().addTravelEvent(travelEvent);
                        return travelEvent;
                    }
                }
            }
        }
        return null;
    }

	// region Events
	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			if (theGalaxy != null) {
				MatterOverdrive.NETWORK.sendTo(new PacketUpdateGalaxy(theGalaxy), (EntityPlayerMP) event.player);
				if (tryAndClaimPlanet(event.player)) {
					Planet planet = getHomeworld(event.player);
					MatterOverdrive.NETWORK.sendToDimention(new PacketUpdatePlanet(planet), event.player.world);
				}
			} else {
				MOLog.warn("Galaxy is missing.");
			}
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load load) {
		if (!load.getWorld().isRemote && load.getWorld().provider.getDimension() == 0) {
			world = load.getWorld();
			File galaxyFile = getGalaxyFile(load.getWorld());
			long start = System.nanoTime();
			if (!loadGalaxy(galaxyFile, load.getWorld())) {
				createGalaxy(galaxyFile, load.getWorld());
				MOLog.info("Galaxy Generated and saved to '%1$s'. Took %2$s milliseconds", galaxyFile.getPath(),
						((System.nanoTime() - start) / 1000000));
			} else {
				MOLog.info("Galaxy Loaded from '%1$s'. Took %2$s milliseconds", galaxyFile.getPath(),
						((System.nanoTime() - start) / 1000000));
			}
		}
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save save) {
		if (save.getWorld() == null || theGalaxy == null) {
			return;
		}

		if (!save.getWorld().isRemote && save.getWorld().provider.getDimension() == 0) {
			if (theGalaxy.isDirty()) {
				long start = System.nanoTime();
				File galaxyFile = getGalaxyFile(save.getWorld());
				if (saveGalaxy(galaxyFile)) {
					theGalaxy.onSave(galaxyFile, save.getWorld());
					MOLog.info("Galaxy saved to '%s'. Took %s milliseconds", galaxyFile.getPath(),
							((System.nanoTime() - start) / 1000000));
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload unload) {
		if (unload.getWorld() == null || theGalaxy == null) {
			return;
		}

		if (unload.getWorld().isRemote && unload.getWorld().provider.getDimension() == 0) {
			this.world = null;
			theGalaxy = null;
			homePlanets.clear();
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.world == null || theGalaxy == null) {
			return;
		}

		if (!event.world.isRemote && event.world.provider.getDimension() == 0) {
			theGalaxy.update(event.world);
		}
	}
	// endregion

	@Override
	public void onConfigChanged(ConfigurationHandler config) {
		Galaxy.GALAXY_BUILD_TIME_MULTIPLY = config.config.getFloat("galaxy build time multiply",
				ConfigurationHandler.CATEGORY_STARMAP, 1, 0, 10,
				"The multiplier for the building and ship building times");
		Galaxy.GALAXY_TRAVEL_TIME_MULTIPLY = config.config.getFloat("galaxy travel time multiply",
				ConfigurationHandler.CATEGORY_STARMAP, 1, 0, 10, "The multiplier for the ship travel times");
	}
	public GalaxyGenerator getGalaxyGenerator(){return galaxyGenerator;}
	
	// region Getters and Setters
	private File getGalaxyFile(World world) {
		File worldDirectory = world.getSaveHandler().getWorldDirectory();
		return new File(worldDirectory.getPath() + "/galaxy.dat");
	}

	// endregion
}
