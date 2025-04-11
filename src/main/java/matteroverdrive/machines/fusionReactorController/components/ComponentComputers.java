
package matteroverdrive.machines.fusionReactorController.components;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;

import java.util.Arrays;
import java.util.EnumSet;

@Optional.InterfaceList({
		@Optional.Interface(modid = "computercraft", iface = "dan200.computercraft.api.peripheral.IPeripheralProvider")
})
public class ComponentComputers extends MachineComponentAbstract<TileEntityMachineFusionReactorController>  implements IPeripheral, SimpleComponent
{

	private String[] methodNames = new String[] { "getStatus", "isValid", "getEnergyGenerated", "getMatterUsed",
			"getEnergyStored", "getMatterStored" };
	private String peripheralName = "mo_fusion_reactor_controller";

	public ComponentComputers(TileEntityMachineFusionReactorController machine) {
		super(machine);
	}

	private Object[] callMethod(int method, Object[] args) {
		switch (method) {
		case 0:
			return computerGetStatus(args);
		case 1:
			return computerIsValid(args);
		case 2:
			return computerGetEnergyGenerated(args);
		case 3:
			return computerGetMatterUsed(args);
		case 4:
			return computerGetEnergyStored(args);
		case 5:
			return computerGetMatterStored(args);
		default:
			throw new IllegalArgumentException("Invalid method id");
		}
	}

	private Object[] computerGetStatus(Object[] args) {
		return new Object[] { machine.getMonitorInfo() };
	}

	private Object[] computerIsValid(Object[] args) {
		return new Object[] { machine.isValidStructure() };
	}

	private Object[] computerGetEnergyGenerated(Object[] args) {
		return new Object[] { machine.getEnergyPerTick() };
	}

	private Object[] computerGetMatterUsed(Object[] args) {
		return new Object[] { machine.getMatterDrainPerTick() };
	}

	private Object[] computerGetEnergyStored(Object[] args) {
		return new Object[] { machine.getEnergyStorage().getEnergyStored() };
	}

	private Object[] computerGetMatterStored(Object[] args) {
		return new Object[] {
				machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).getMatterStored() };
	}

	 //region ComputerCraft
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public String getType() { return
	  peripheralName; }
	 
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public String[] getMethodNames() {
	  return methodNames; }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public Object[]
	  callMethod(IComputerAccess computer, ILuaContext context, int method,
	  Object[] arguments) throws LuaException, InterruptedException { try { return
	  callMethod(method, arguments); } catch (Exception e) { throw new
	  LuaException(e.getMessage()); } }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public void attach(IComputerAccess
	  computer) {
	  
	  }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public void detach(IComputerAccess
	  computer) {
	  
	  }
	  
	  @Override
	  
	  @Optional.Method(modid = "computercraft") public boolean equals(IPeripheral
	  other) { return false; }
	  	  
	  @Optional.Method(modid = "opencomputers") public String[] methods() { return
	  methodNames; }
	  
	  @Optional.Method(modid = "opencomputers") public Object[] invoke(String
	  method, Context context, Arguments args) throws Exception { int methodId =
	  Arrays.asList(methodNames).indexOf(method);
	  
	  if (methodId == -1) { throw new RuntimeException("The method " + method +
	  " does not exist"); }
	  
	  return callMethod(methodId, args.toArray()); }
	  
	  @Optional.Method(modid = "opencomputers") public String getComponentName() {
	  return peripheralName; }

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
