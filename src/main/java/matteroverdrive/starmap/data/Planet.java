
package matteroverdrive.starmap.data;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.api.starmap.IBuildable;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.client.data.Color;
import matteroverdrive.network.packet.client.starmap.PacketUpdatePlanet;
import matteroverdrive.starmap.GalaxyGenerator;
import matteroverdrive.starmap.gen.ISpaceBodyGen;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Planet extends SpaceBody implements IInventory {

	public static final int SLOT_COUNT = 4;
	private Star star;
	private float size, orbit;
	private byte type;
	private UUID ownerUUID;
	private NonNullList<ItemStack> inventory;
	private List<ItemStack> buildings;
	private List<ItemStack> fleet;
	private boolean isDirty, homeworld, generated, needsClientUpdate;
	private int buildingSpaces, fleetSpaces, seed;

	public int getBuildingSpaces() {
		return (int) getStatChangeFromBuildings(PlanetStatType.BUILDINGS_SIZE, buildingSpaces);
	}

	public void setBuildingSpaces(int buildingSpaces) {
		this.buildingSpaces = buildingSpaces;
	}

	public List<ItemStack> getBuildings() {
		return buildings;
	}

	public List<ItemStack> getFleet() {
		return fleet;
	}
    public int getFactoryCount()
    {
        return buildings.size();
    }

	public int getFleetSpaces() {
		return (int) getStatChangeFromBuildings(PlanetStatType.FLEET_SIZE, fleetSpaces);
	}

	public void setFleetSpaces(int fleetSpaces) {
		this.fleetSpaces = fleetSpaces;
	}

	public ItemStack getShip(int at) {
		return fleet.get(at);
	}

	public int getPowerProducation() {
		return (int) getStatChangeFromBuildings(PlanetStatType.ENERGY_PRODUCTION, 0);
	}

	// region Constructors
	public Planet() {
		super();
		init();
	}

	public float getStatChangeFromBuildings(PlanetStatType statType, float original) {
		for (ItemStack building : getBuildings()) {
			if (building.getItem() instanceof IPlanetStatChange) {
				original = ((IPlanetStatChange) building.getItem()).changeStat(building, this, statType, original);
			}
		}
		return original;
	}

	public Planet(String name, int id) {
		super(name, id);
		init();
	}

	@SideOnly(Side.CLIENT)
	public static Color getGuiColor(Planet planet) {
		if (planet.hasOwner()) {
			if (planet.getOwnerUUID().equals(EntityPlayer.getUUID(Minecraft.getMinecraft().player.getGameProfile()))) {
				if (planet.isHomeworld()) {
					return Reference.COLOR_HOLO_YELLOW;
				} else {
					return Reference.COLOR_HOLO_GREEN;
				}
			} else {
				return Reference.COLOR_HOLO_RED;
			}
		} else {
			return Reference.COLOR_HOLO;
		}
	}
	// endregion

	@Override
	public boolean isEmpty() {
		return false;
	}

	private void init() {
		inventory = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        buildings = new ArrayList<ItemStack>();
        fleet = new ArrayList<ItemStack>();
	}
	// endregion

	// region Updates
	public void update(World world) {
		if (!world.isRemote) {
			if (needsClientUpdate) {
				needsClientUpdate = false;
				MatterOverdrive.NETWORK.sendToDimention(new PacketUpdatePlanet(this), world);
			}
			for (int i = 0;i < SLOT_COUNT;i++)
            {
                List<String> buildInfo = new ArrayList<>();
                ItemStack buildableStack = getStackInSlot(i);
                if (buildableStack != null)
                {
                    if (buildableStack.getItem() instanceof IBuilding)
                    {
                        //check if building can be built and if build start is above zero
                        //if below zero then the building was just put in
                        if (canBuild((IBuilding) buildableStack.getItem(), buildableStack, buildInfo)) {
                            if (((IBuilding) buildableStack.getItem()).isReadyToBuild(world, buildableStack, this)) {
                                buildings.add(buildableStack);
                                if (getOwnerUUID() != null)
                                {
                                    ((IBuilding)buildableStack.getItem()).setOwner(buildableStack,getOwnerUUID());
                                }
                                setInventorySlotContents(i, null);
                                onBuild((IBuilding)buildableStack.getItem(),buildableStack,world);
                                markDirty();
                            }
                        }else
                        {
                            //resets the build start time
                            ((IBuilding) buildableStack.getItem()).setBuildStart(buildableStack,world.getTotalWorldTime());
                            markDirty();
                        }
                    }
                    else if (buildableStack.getItem() instanceof IShip)
                    {
                        //check if ship can be built and if build start is above zero
                        //if below zero then the ship was just put in
                        if (canBuild((IShip) buildableStack.getItem(), buildableStack, buildInfo)) {
                            if (((IShip) buildableStack.getItem()).isReadyToBuild(world, buildableStack, this)) {
                                fleet.add(buildableStack);
                                if (getOwnerUUID() != null)
                                {
                                    ((IShip) buildableStack.getItem()).setOwner(buildableStack, getOwnerUUID());
                                }
                                setInventorySlotContents(i, null);
                                onBuild((IShip) buildableStack.getItem(), buildableStack,world);
                                markDirty();
                            }
                        }else
                        {
                            //resets the build start time
                            ((IShip) buildableStack.getItem()).setBuildStart(buildableStack,world.getTotalWorldTime());
                            markDirty();
                        }
                    }
                }
            }
        }
    }
    public void onBuild(IBuildable buildable,ItemStack buildableStack,World world)
    {
        UUID ownerID = buildable.getOwnerID(buildableStack);
        if (ownerID != null)
        {
            EntityPlayer entityPlayer = world.getPlayerEntityByUUID(ownerID);
            if(entityPlayer != null) {
            	entityPlayer.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + Reference.MOD_NAME + "]" + TextFormatting.RESET + String.format(MOStringHelper.translateToLocal("alert.starmap.on_build"), buildableStack.getDisplayName(), name)));
            }
        }
    }
    
	// region Events
	public void onSave(File file, World world) {
		isDirty = false;
	}

	public void onTravelEvent(ItemStack ship, GalacticPosition from, World world) {
	        if (!world.isRemote)
	        {
	            if (ship.getItem() instanceof IShip)
	            {
	                UUID ownerID = ((IShip) ship.getItem()).getOwnerID(ship);
	                if (ownerID != null)
	                {
	                    EntityPlayer owner = world.getPlayerEntityByUUID(ownerID);
	                    if (owner != null) {
	                    owner.sendMessage(new TextComponentString(TextFormatting.GOLD + "[" + Reference.MOD_NAME + "]" + TextFormatting.RESET + String.format(MOStringHelper.translateToLocal("alert.starmap.ship_arrive"), ship.getDisplayName(), name)
	                                )
	                        );
	                    }
	                }

	                ((IShip) ship.getItem()).onTravel(ship,this);
	                if (ship.getCount() <= 0)
	                {
	                    removeShip(ship);
	                }

	                markDirty();
	                markForUpdate();
	            }
	        }
	    }
	// endregion

	// region Read - Write
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		for (int i = 0; i < getSizeInventory(); i++) {
			if (!getStackInSlot(i).isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				getStackInSlot(i).writeToNBT(itemTag);
				tagCompound.setTag("Slot" + i, itemTag);
			}
		}
        for (int i = 0;i < buildings.size();i++)
        {
            NBTTagCompound buiildingTAG = new NBTTagCompound();
            buildings.get(i).writeToNBT(buiildingTAG);
            tagCompound.setTag("Building" + i, buiildingTAG);
        }
        for (int i = 0;i < fleet.size();i++)
        {
            NBTTagCompound shipTag = new NBTTagCompound();
            fleet.get(i).writeToNBT(shipTag);
            tagCompound.setTag("Ship"+i,shipTag);
        }
		if (ownerUUID != null) {
			tagCompound.setString("OwnerUUID", ownerUUID.toString());
		}

		tagCompound.setBoolean("Homeworld", homeworld);
		tagCompound.setFloat("Size", size);
		tagCompound.setByte("Type", type);
		tagCompound.setFloat("Orbit", orbit);
        tagCompound.setInteger("BuildingSpaces", buildingSpaces);
        tagCompound.setInteger("FleetSpaces", fleetSpaces);
		tagCompound.setInteger("Seed", seed);
	}

	@Override
	public void writeToBuffer(ByteBuf byteBuf) {
		super.writeToBuffer(byteBuf);
		NBTTagCompound nbtData = new NBTTagCompound();
		writeToNBT(nbtData);
		ByteBufUtils.writeTag(byteBuf, nbtData);
	}

	public void readFromNBT(NBTTagCompound tagCompound, GalaxyGenerator generator) {
		super.readFromNBT(tagCompound, generator);
		buildings.clear();
        fleet.clear();
		for (int i = 0; i < getSizeInventory(); i++) {
			if (tagCompound.hasKey("Slot" + i, 10)) {
				//setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Slot" + i)));
				setInventorySlotContents(i, new ItemStack(tagCompound.getCompoundTag("Slot" + i)));
			}
		}
        buildingSpaces = tagCompound.getInteger("BuildingSpaces");
        for (int i = 0;i < getBuildingSpaces();i++)
        {
            if (tagCompound.hasKey("Building" + i,10))
            {
            	System.out.println("Building: " + (tagCompound.getCompoundTag("Building" + i)));
            	//ItemStack buildingStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Building" + i));
                ItemStack buildingStack = new ItemStack(tagCompound.getCompoundTag("Building" + i));
                System.out.println("buildingStack: " + buildingStack);
                if (buildingStack != null) {
                    addBuilding(buildingStack);
               } else  {
					MatterOverdrive.LOGGER.error("There was a problem loading a building from NBT of planet ", getName());
                }
            }
        }
        fleetSpaces = tagCompound.getInteger("FleetSpaces");
        for (int i = 0;i < getFleetSpaces();i++)
        {
            if (tagCompound.hasKey("Ship"+i,10))
            {
            	//ItemStack shipStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Ship" + i));
                ItemStack shipStack = new ItemStack(tagCompound.getCompoundTag("Ship" + i));
                System.out.println("Stack: " + shipStack);
                if (shipStack != null)
                {
                    addShip(shipStack);
                }else
                {
					MatterOverdrive.LOGGER.error("There was a problem loading a ship from NBT of planet ", getName());
                }
            }
        }
		if (tagCompound.hasKey("OwnerUUID", 8)) {
			try {
				ownerUUID = UUID.fromString(tagCompound.getString("OwnerUUID"));
			} catch (IllegalArgumentException e) {
				MOLog.log(Level.ERROR, e, "Invalid planet owner UUID '" + tagCompound.getString("OwnerUUID") + "'",
						this);
			}

		}
		homeworld = tagCompound.getBoolean("Homeworld");
		size = tagCompound.getFloat("Size");
		type = tagCompound.getByte("Type");
		orbit = tagCompound.getFloat("Orbit");
		seed = tagCompound.getInteger("Seed");

		generateMissing(tagCompound, generator);
	}

	@Override
	public void readFromBuffer(ByteBuf byteBuf) {
		super.readFromBuffer(byteBuf);
		NBTTagCompound nbtData = ByteBufUtils.readTag(byteBuf);
		readFromNBT(nbtData, null);
	}
	// endregion

	public void generateMissing(NBTTagCompound tagCompound, GalaxyGenerator galaxyGenerator) {
		if (galaxyGenerator != null) {
			for (ISpaceBodyGen<Planet> starGen : galaxyGenerator.getPlanetGen().getGens()) {
				galaxyGenerator.getStarRandom().setSeed(seed);
				if (starGen.generateMissing(tagCompound, this, galaxyGenerator.getStarRandom())) {
					break;
				}
			}
		}
	}

	// region Getters and Setters
	@Override
	public SpaceBody getParent() {
		return star;
	}

	public Star getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
	}

	public void setOwner(EntityPlayer player) {
		ownerUUID = EntityPlayer.getUUID(player.getGameProfile());
	}

	public boolean hasOwner() {
		return ownerUUID != null;
	}

	public boolean isOwner(EntityPlayer player) {
		return hasOwner() && getOwnerUUID().equals(EntityPlayer.getUUID(player.getGameProfile()));
	}

	public boolean isHomeworld() {
		return homeworld;
	}

	public void setHomeworld(boolean homeworld) {
		this.homeworld = homeworld;
	}

	public boolean isHomeworld(EntityPlayer player) {
		return isOwner(player) && isHomeworld();
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public float getOrbit() {
		return orbit;
	}

	public void setOrbit(float orbit) {
		this.orbit = orbit;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	public int getPopulation() {
		return 0;
	}

	public int getPowerProduction() {
		return 0;
	}

	public float getMatterProduction() {
		return 0;
	}

	public float getHappiness() {
		return 0;
	}

	public void markForUpdate() {
		needsClientUpdate = true;
	}

	public boolean isDirty() {
		return this.isDirty;
	}
	// endregion

	// region Inventory
	@Override
	public int getSizeInventory() {
		return SLOT_COUNT;
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		if (slot >= 0 && slot < getSizeInventory()) {
			return inventory.get(slot);
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int slot, int size) {
		if (!inventory.get(slot).isEmpty()) {
			ItemStack itemstack;

			if (inventory.get(slot).getCount() <= size) {
				itemstack = inventory.get(slot);
				inventory.set(slot, ItemStack.EMPTY);

				return itemstack;
			} else {
				itemstack = inventory.get(slot).splitStack(size);

				if (inventory.get(slot).getCount() == 0) {
					inventory.set(slot, ItemStack.EMPTY);
				}

				return itemstack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (!inventory.get(index).isEmpty()) {
			ItemStack stack = inventory.get(index);
			inventory.set(index, ItemStack.EMPTY);
			return stack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
		if (slot < inventory.size()) {
			inventory.set(slot, stack);

			if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
				stack.setCount(this.getInventoryStackLimit());
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		isDirty = true;
		markForUpdate();
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
		return !hasOwner() || getOwnerUUID().equals(EntityPlayer.getUUID(player.getGameProfile()));

	}

	@Override
	public void openInventory(@Nonnull EntityPlayer player) {

	}

	@Override
	public void closeInventory(@Nonnull EntityPlayer player) {

	}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot < SLOT_COUNT/2)
        {
            return stack.getItem() instanceof IBuilding;
        }else
        {
            return stack.getItem() instanceof IShip;
        }
    }
    
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Nonnull
	@Override
	public String getName() {
		return getSpaceBodyName();
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getSpaceBodyName());
	}

	public boolean canBuild(IBuildable buildable, ItemStack stack, List<String> info) {
		if (buildable instanceof IBuilding) {
			return canBuild((IBuilding) buildable, stack, info);
		} else if (buildable instanceof IShip) {
			return canBuild((IShip) buildable, stack, info);
		} else
			return false;
	}

	public boolean canBuild(IBuilding building, ItemStack stack, List<String> info) {
		if (buildings.size() < getBuildingSpaces()) {
			if (hasBuildingType(BuildingType.BASE)) {
				return building.canBuild(stack, this, info);
			}

			info.add(MOStringHelper.translateToLocal("gui.tooltip.starmap.no_base"));
		}
		info.add(MOStringHelper.translateToLocal("gui.tooltip.starmap.no_building_space"));
		return false;
	}

	public boolean canBuild(IShip ship, ItemStack stack, List<String> info) {
		if (fleet.size() < getFleetSpaces()) {
			if (hasBuildingType(BuildingType.SHIP_FACTORY)) {
				return ship.canBuild(stack, this, info);
			}

			info.add(MOStringHelper.translateToLocal("gui.tooltip.starmap.no_ship_factory"));
		} else {
			info.add(MOStringHelper.translateToLocal("gui.tooltip.starmap.no_ship_space"));
		}
		return false;
	}

	public boolean hasBuildingType(BuildingType buildingType) {
		for (ItemStack building : getBuildings()) {
			if (building.getItem() instanceof IBuilding
					&& ((IBuilding) building.getItem()).getType(building) == buildingType) {
				return true;
			}
		}
		return false;
	}

	// endregion
	public void addShip(ItemStack ship) {
		if (ship != null) {
			if (ship.getItem() instanceof IShip) {
				fleet.add(ship);
			} else {
				MatterOverdrive.LOGGER.error("Trying to add an itemstack to ships, that does not contain a Ship Item");
			}
		} else {
			MatterOverdrive.LOGGER.error("Trying to add a null Ship itemstack to %s", getName());
		}
	}

	public boolean canAddShip(ItemStack ship, @Nullable EntityPlayer player) {
		if (ship != null && ship.getItem() instanceof IShip) {
			if (player != null && hasOwner() && isHomeworld()) {
				return isOwner(player);
			} else {
				if (fleetCount() < getFleetCount()) {
					return true;
				}
			}
		}
		return false;
	}

	public int fleetCount() {
		return fleet.size();
	}

	public int getFleetCount() {
		return fleet.size();
	}

	public ItemStack removeShip(int at) {
		if (at < fleet.size())
			return fleet.remove(at);
		else
			return null;
	}

	public boolean removeShip(ItemStack ship) {
		return fleet.remove(ship);
	}

	public void addBuilding(@Nonnull ItemStack building) {
		if (building != null) {
			if (building.getItem() instanceof IBuilding) {
				this.buildings.add(building);
			} else {
				MatterOverdrive.LOGGER.error("Trying to add a stack to buildings, that does not contain a Building Item");
			}
		} else {
			MatterOverdrive.LOGGER.error("Trying to add a null building to planet %s", getName());
		}
	}
}
