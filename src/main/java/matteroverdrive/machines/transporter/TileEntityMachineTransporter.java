
package matteroverdrive.machines.transporter;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.MOEventTransport;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.transport.ITransportList;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.TeleportFlashDriveSlot;
import matteroverdrive.fx.ReplicatorParticle;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.machines.transporter.components.ComponentComputers;
import matteroverdrive.network.packet.client.PacketSyncTransportProgress;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.util.math.MOMathHelper;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.Optional;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Optional.InterfaceList({
		@Optional.Interface(modid = "computercraft", iface = "dan200.computercraft.api.peripheral.IPeripheral"),
		//@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.SimpleComponent"),
		//@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.ManagedPeripheral")
})
public class TileEntityMachineTransporter extends MOTileEntityMachineMatter implements ITransportList ,
																										// IWailaBodyProvider,
																										 IPeripheral
																										// SimpleComponent,
																										// ManagedPeripheral
{
	public static int MATTER_PER_TRANSPORT = 25;
	public static final int MAX_ENTITIES_PER_TRANSPORT = 3;
	public static final int TRANSPORT_TIME = 70;
	public static final int TRANSPORT_DELAY = 80;
	public static final int ENERGY_CAPACITY = 512000;
	public static final int ENERGY_TRANSFER = 512000;
	public static int ENERGY_PER_UNIT = 16;
	private static final EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.PowerUsage, UpgradeTypes.Speed,
			UpgradeTypes.Range, UpgradeTypes.PowerStorage, UpgradeTypes.Muffler);
	private static final int TRANSPORT_RANGE = 32;
	public static int MATTER_STORAGE = 512;
	public final List<TransportLocation> locations;
	public int selectedLocation;
	public int usbSlotID;
	int transportTimer;
	long transportTracker;
	private ComponentComputers computerComponent;

	public TileEntityMachineTransporter() {
		super(5);
		this.energyStorage.setCapacity(ENERGY_CAPACITY);
		this.energyStorage.setMaxExtract(ENERGY_TRANSFER);
		this.energyStorage.setMaxReceive(ENERGY_TRANSFER);

		this.matterStorage.setCapacity(MATTER_STORAGE);
		this.matterStorage.setMaxReceive(MATTER_STORAGE);
		this.matterStorage.setMaxExtract(0);

		locations = new ArrayList<>();
		selectedLocation = 0;
		playerSlotsHotbar = true;
	}

	@Override
	protected void RegisterSlots(Inventory inventory) {
		super.RegisterSlots(inventory);
		usbSlotID = inventory.AddSlot(new TeleportFlashDriveSlot(true));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
		super.writeCustomNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.CONFIGS)) {
			writeLocations(nbt);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.CONFIGS)) {
			readLocations(nbt);
		}
	}

	public void readLocations(NBTTagCompound nbt) {
		locations.clear();
		NBTTagList locationsList = nbt.getTagList("transportLocations", 10);
		for (int i = 0; i < locationsList.tagCount(); i++) {
			locations.add(new TransportLocation(locationsList.getCompoundTagAt(i)));
		}
		selectedLocation = nbt.getInteger("selectedTransport");
	}

	public void writeLocations(NBTTagCompound nbt) {
		NBTTagList locationsList = new NBTTagList();
		for (TransportLocation location : locations) {
			NBTTagCompound positionTag = new NBTTagCompound();
			location.writeToNBT(positionTag);
			locationsList.appendTag(positionTag);
		}
		nbt.setTag("transportLocations", locationsList);
		nbt.setInteger("selectedTransport", selectedLocation);
	}

	@Override
	public void writeToDropItem(ItemStack itemStack) {
		super.writeToDropItem(itemStack);
		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		writeLocations(itemStack.getTagCompound());
	}

	@Override
	public void update() {
		super.update();
		manageTeleportation();
	}

	@Override
	protected void registerComponents() {
		super.registerComponents();
		computerComponent = new ComponentComputers(this);
		addComponent(computerComponent);
	}

	void manageTeleportation() {
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(getPos(), getPos().add(1, 2, 1)));
		TransportLocation position = getSelectedLocation();

		if (!world.isRemote) {
			if (getEnergyStorage().getEnergyStored() >= getEnergyDrain()
					&& getMatterStorage().getMatterStored() >= getMatterDrain(entities.size()) && entities.size() > 0
					&& isLocationValid(getSelectedLocation()) && getRedstoneActive()) {
				if (transportTracker < world.getTotalWorldTime()) {
					transportTimer++;

					if (transportTimer >= getSpeed()) {
						for (int i = 0; i < Math.min(entities.size(), MAX_ENTITIES_PER_TRANSPORT); i++) {
							Teleport(entities.get(i), position);
							transportTracker = world.getTotalWorldTime() + getTransportDelay();
						}

						energyStorage.modifyEnergyStored(-getEnergyDrain());
						matterStorage.modifyMatterStored(-getMatterDrain(entities.size()));

						transportTimer = 0;
						MatterOverdrive.NETWORK.sendToDimention(new PacketSyncTransportProgress(this), world);
					} else {
						MatterOverdrive.NETWORK.sendToAllAround(new PacketSyncTransportProgress(this), this,
								TRANSPORT_RANGE);
					}
				}
			} else {
				if (transportTimer != 0) {
					transportTimer = 0;
					MatterOverdrive.NETWORK.sendToDimention(new PacketSyncTransportProgress(this), world);
				}
			}
		} else {
			if (transportTimer > 0) {
				for (Entity entity : entities) {
					SpawnReplicateParticles(entity,
							new Vector3f((float) entity.posX, getPos().getY(), (float) entity.posZ));
				}
				for (Entity entity : entities) {
					SpawnReplicateParticles(entity,
							new Vector3f(position.pos.getX(), position.pos.getY(), position.pos.getZ()));
				}
			}
		}
	}

	@Override
	protected void onMachineEvent(MachineEvent event) {

	}

	public void Teleport(Entity entity, TransportLocation position) {
		if (!MinecraftForge.EVENT_BUS.post(new MOEventTransport(getPos(), position, entity))) {
			float x = position.pos.getX() + 0.5f;
			float y = position.pos.getY();
			float z = position.pos.getZ() + 0.5f;

			if (entity instanceof EntityLivingBase) {
				entity.setPositionAndUpdate(x, y, z);
			} else {
				entity.setPosition(x, y, z);
			}
		}
	}

	public TransportLocation getSelectedLocation() {
		if (selectedLocation < locations.size() && selectedLocation >= 0) {
			TransportLocation location = locations.get(selectedLocation);
			return location;
		}
		return new TransportLocation(getPos(), "Unknown");
	}

	public boolean isLocationValid(TransportLocation location) {
		return !(location.pos.getX() == getPos().getX() && location.pos.getY() < getPos().getY() + 4
				&& location.pos.getY() > getPos().getY() - 4 && location.pos.getZ() == getPos().getZ())
				&& location.getDistance(getPos()) < getTransportRange();
	}

	public void setSelectedLocation(BlockPos pos, String name) {
		if (selectedLocation < locations.size() && selectedLocation >= 0) {
			TransportLocation location = locations.get(selectedLocation);
			if (location != null) {
				location.setPosition(pos);
				location.setName(name);
			} else {
				locations.set(selectedLocation, new TransportLocation(pos, name));
			}

		} else {
			selectedLocation = 0;
			locations.add(new TransportLocation(pos, name));
		}
	}

	public void addNewLocation(BlockPos pos, String name) {
		locations.add(new TransportLocation(pos, name));
	}

	public void removeLocation(int at) {
		if (at < locations.size() && at >= 0) {
			locations.remove(at);
			selectedLocation = MathHelper.clamp(selectedLocation, 0, locations.size() - 1);
		}
	}

	@SideOnly(Side.CLIENT)
	public void SpawnReplicateParticles(Entity entity, Vector3f p) {
		double entityRadius = entity.width;
		double entityArea = Math.max(entityRadius * entity.height, 0.3);

		double radiusX = entityRadius + random.nextDouble() * 0.2f;
		double radiusZ = entityRadius + random.nextDouble() * 0.2f;
		double time = Math.min((double) (transportTimer) / (double) (getTransportDelay()), 1);
		double gravity = 0.015f;
		int age = (int) Math.round(MOMathHelper.easeIn(time, 5, 15, 1));
		int count = (int) Math.round(MOMathHelper.easeIn(time, 2, entityArea * 15, 1));

		for (int i = 0; i < count; i++) {
			float speed = random.nextFloat() * 0.05f + 0.15f;
			float height = p.y + random.nextFloat() * entity.height;

			Vector3f origin = new Vector3f(p.x, height, p.z);
			Vector3f pos = MOMathHelper.randomSpherePoint(origin.x, origin.y, origin.z, new Vec3d(radiusX, 0, radiusZ),
					random);
			Vector3f dir = Vector3f.cross(Vector3f.sub(origin, pos, null), new Vector3f(0, 0, 0), null);
			dir.scale(speed);
			ReplicatorParticle replicatorParticle = new ReplicatorParticle(this.world, pos.x, pos.y, pos.z, dir.x,
					dir.y, dir.z);
			replicatorParticle.setCenter(origin.x, origin.y, origin.z);

			replicatorParticle.setParticleAge(age);
			replicatorParticle.setPointGravityScale(gravity);

			Minecraft.getMinecraft().effectRenderer.addEffect(replicatorParticle);
		}
	}

	public int getEnergyDrain() {
		TransportLocation location = getSelectedLocation();
		return (int) Math.round(
				getUpgradeMultiply(UpgradeTypes.PowerUsage) * (location.getDistance(getPos()) * ENERGY_PER_UNIT));
	}

	public int getMatterDrain(int numEntities) {
		return MATTER_PER_TRANSPORT * numEntities;
	}

	private int getSpeed() {
		return (int) Math.round(getUpgradeMultiply(UpgradeTypes.Speed) * TRANSPORT_TIME);
	}

	private int getTransportDelay() {
		return (int) Math.round(getUpgradeMultiply(UpgradeTypes.Speed) * TRANSPORT_DELAY);
	}

	public int getTransportRange() {
		return (int) Math.round(getUpgradeMultiply(UpgradeTypes.Range) * TRANSPORT_RANGE);
	}

	@Override
	public SoundEvent getSound() {
		return MatterOverdriveSounds.transporter;
	}

	@Override
	public boolean hasSound() {
		return true;
	}

	@Override
	public boolean getServerActive() {
		return transportTimer > 0;
	}

	@Override
	public float soundVolume() {
		return 0.5f;
	}

//	@Override
//	public boolean canFill(EnumFacing from, Fluid fluid)
//	{
//		return from != EnumFacing.UP && super.canFill(from, fluid);
//	}
//
//	@Override
//	public boolean canDrain(EnumFacing from, Fluid fluid)
//	{
//		return from != EnumFacing.UP && super.canDrain(from, fluid);
//	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type) {
		return upgradeTypes.contains(type);
	}

	public int getTransportTime() {
		return transportTimer;
	}

	public void setTransportTime(int time) {
		transportTimer = time;
	}

	@Override
	public List<TransportLocation> getPositions() {
		return locations;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (facing != EnumFacing.UP && (capability == MatterOverdriveCapabilities.MATTER_HANDLER
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != EnumFacing.UP && (capability == MatterOverdriveCapabilities.MATTER_HANDLER
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
			return (T) matterStorage;
		}
		return super.getCapability(capability, facing);
	}

	
	  //region All Computers
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public String getType() { return
	  computerComponent.getType(); }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public String[] getMethodNames() {
	  return computerComponent.getMethodNames(); }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public Object[]
	  callMethod(IComputerAccess computer, ILuaContext context, int method,
	  Object[] arguments) throws LuaException, InterruptedException { return
	  computerComponent.callMethod(computer,context,method,arguments); }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public void attach(IComputerAccess
	  computer) { computerComponent.attach(computer); }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public void detach(IComputerAccess
	  computer) { computerComponent.attach(computer); }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public boolean equals(IPeripheral
	  other) { // Does this mean if it's the same type or if they're the same one?
	  return computerComponent.equals(other); }
	  
	 /* 
	 * @Override
	 * 
	 * @Optional.Method(modid = "OpenComputers") public String getComponentName() {
	 * return computerComponent.getComponentName(); }
	 * 
	 * @Override
	 * 
	 * @Optional.Method(modid = "OpenComputers") public String[] methods() { return
	 * computerComponent.methods(); }
	 * 
	 * @Override
	 * 
	 * @Optional.Method(modid = "OpenComputers") public Object[] invoke(String
	 * method, Context context, Arguments args) throws Exception { return
	 * computerComponent.invoke(method,context,args); }
	 * 
	 */
}
