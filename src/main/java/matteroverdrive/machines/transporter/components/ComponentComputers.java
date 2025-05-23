
package matteroverdrive.machines.transporter.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.SimpleComponent;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.configs.IConfigProperty;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;

import net.minecraftforge.fml.common.Optional;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

@Optional.InterfaceList({
		@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft"),
		@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers"),
		@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "opencomputers") })
@SimpleComponent.SkipInjection
public class ComponentComputers extends MachineComponentAbstract<TileEntityMachineTransporter>
		implements IPeripheral, SimpleComponent, ManagedPeripheral {

	private static String[] methodNames = new String[] { "getLocations", "getSelectedLocation", "getLocation",
			"addLocation", "setSelectedLocation", "setName", "setX", "setY", "setZ", "setRedstoneMode" };
	private String peripheralName = "mo_transporter";

	public ComponentComputers(TileEntityMachineTransporter machine) {
		super(machine);
	}

	private Object[] callMethod(int method, Object[] args) {
		switch (method) {
		case 0:
			return computerGetLocations(args);
		case 1:
			return computerGetSelectedLocation(args);
		case 2:
			return computerGetLocation(args);
		case 3:
			return computerAddLocation(args);
		case 4:
			return computerSetSelectedLocation(args);
		case 5:
			return computerSetName(args);
		case 6:
			return computerSetX(args);
		case 7:
			return computerSetY(args);
		case 8:
			return computerSetZ(args);
		case 9:
			return computerSetRedstoneMode(args);
		default:
			throw new IllegalArgumentException("Invalid method id");
		}
	}

	private Object[] computerGetLocations(Object[] args) {

		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		for (TransportLocation loc : machine.getPositions()) {
			HashMap<String, Object> map = new HashMap<>();

			map.put("name", loc.name);
			map.put("selected", machine.selectedLocation == machine.getPositions().indexOf(loc));
			map.put("x", loc.pos.getX());
			map.put("y", loc.pos.getY());
			map.put("z", loc.pos.getZ());

			list.add(map);
		}

		return list.toArray();
	}

	private Object[] computerGetSelectedLocation(Object[] args) {
		return computerGetLocation(new Object[] { 1.0 });
	}

	/**
	 * args: id (number) numeric index of the location to select (First location has
	 * index 0)
	 */
	private Object[] computerGetLocation(Object[] args) {

		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("First argument must be the numerical id of the transport location");
		}

		int locNum = (int) Math.floor((Double) args[0]);

		HashMap<String, Object> map = new HashMap<>();

		TransportLocation loc = machine.getPositions().get(locNum);
		map.put("name", loc.name);
		map.put("x", loc.pos.getX());
		map.put("y", loc.pos.getY());
		map.put("z", loc.pos.getZ());

		return new Object[] { map };
	}

	/**
	 * args: name (string) name of the new location x (number) x of the new location
	 * y (number) y of the new location z (number) z of the new location
	 * 
	 */
	private Object[] computerAddLocation(Object[] args) {
		if (!(args[0] instanceof String)) {
			throw new IllegalArgumentException(
					"First argument must be a string containing the name of the transport location");
		}
		for (int i = 1; i <= 4; i++) {
			if (!(args[i] instanceof Double)) {
				throw new IllegalArgumentException("Argument " + i + 1 + " must be an integer");
			}
		}
		String name = (String) args[0];
		int x = (int) Math.floor((Double) args[1]);
		int y = (int) Math.floor((Double) args[2]);
		int z = (int) Math.floor((Double) args[3]);
		machine.addNewLocation(new BlockPos(x, y, z), name);
		return null;
	}

	/**
	 * args: id (number) numeric index of the location to select (First location has
	 * index 0)
	 */

	private Object[] computerSetSelectedLocation(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		machine.selectedLocation = (int) Math.floor((Double) args[0]);
		return null;
	}

	/**
	 * args: id (number) numeric index of the location to select (First location has
	 * index 0) name (string) the new name
	 */

	private Object[] computerSetName(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof String)) {
			throw new IllegalArgumentException("Argument 2 must be a string");
		}

		int locNum = (int) Math.floor((Double) args[0]);

		machine.getPositions().get(locNum).name = (String) args[1];

		return null;
	}

	/**
	 * args: id (number) numeric index of the location to select (first location has
	 * index 0) x (number) the new X inate
	 */

	private Object[] computerSetX(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof Double)) {
			throw new IllegalArgumentException("Argument 2 must be a number");
		}

		int locNum = (int) Math.floor((Double) args[0]);
		// machine.getPositions().get(locNum).pos = (int)Math.floor((Double)args[1]);

		return null;
	}

	/**
	 * args: id (number) numeric index of the location to select (first location has
	 * index 0) y (number) the new Y inate
	 */

	private Object[] computerSetY(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof Double)) {
			throw new IllegalArgumentException("Argument 2 must be a number");
		}

		int locNum = (int) Math.floor((Double) args[0]);
		// machine.getPositions().get(locNum).pos = (int)Math.floor((Double)args[1]);

		return null;
	}

	/**
	 * args: id (number) numeric index of the location to select (first location has
	 * index 0) z (number) the new Z inate
	 */
	private Object[] computerSetZ(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number");
		}
		if (!(args[1] instanceof Double)) {
			throw new IllegalArgumentException("Argument 2 must be a number");
		}

		int locNum = (int) Math.floor((Double) args[0]);
		// machine.getPositions().get(locNum).z = (int)Math.floor((Double)args[1]);

		return null;
	}

	/**
	 * args: mode (number) the redstone mode of the transporter 0: High redstone
	 * signal 1: Low redstone signal 2: Redstone disabled
	 */
	private Object[] computerSetRedstoneMode(Object[] args) {
		if (!(args[0] instanceof Double)) {
			throw new IllegalArgumentException("Argument 1 must be a number from 0 to 2");
		}

		int i = (int) Math.floor((Double) args[0]);

		if (i < 0 || i > 2) {
			throw new IllegalArgumentException("Argument 1 must be a number from 0 to 2");
		}

		IConfigProperty property = machine.getConfigs().getProperty(Reference.CONFIG_KEY_REDSTONE_MODE);
		if (property != null) {
			property.setValue(i);
		} else {
			throw new IllegalArgumentException("No redstone mode config found for machine");
		}

		return null;
	}

	@Override

	@Optional.Method(modid = "computercraft")
	public String getType() {
		return peripheralName;
	}

	@Override

	@Optional.Method(modid = "computercraft")
	public String[] getMethodNames() {
		return methodNames;
	}

	@Override

	@Optional.Method(modid = "computercraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		try {
			return callMethod(method, arguments);
		} catch (Exception e) {
			throw new LuaException(e.getMessage());
		}
	}

	@Override

	@Optional.Method(modid = "computercraft")
	public void attach(IComputerAccess computer) {

	}

	@Override

	@Optional.Method(modid = "computercraft")
	public void detach(IComputerAccess computer) {

	}

	@Override

	@Optional.Method(modid = "computercraft")
	public boolean equals(IPeripheral other) {
		return false;
	}

	@Optional.Method(modid = "opencomputers")
	public String[] methods() {
		return methodNames;
	}

	@Optional.Method(modid = "opencomputers")
	public Object[] invoke(String method, Context context, Arguments args) throws Exception {
		int methodId = Arrays.asList(methodNames).indexOf(method);

		if (methodId == -1) {
			throw new RuntimeException("The method " + method + " does not exist");
		}

		return callMethod(methodId, args.toArray());
	}

	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return peripheralName;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {

	}

	@Override
	public void registerSlots(Inventory inventory) {

	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type) {
		return false;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public void onMachineEvent(MachineEvent event) {

	}

}
