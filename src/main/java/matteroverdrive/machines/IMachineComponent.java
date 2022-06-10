
package matteroverdrive.machines;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;

public interface IMachineComponent {
	void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories);

	void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk);

	void registerSlots(Inventory inventory);

	boolean isAffectedByUpgrade(UpgradeTypes type);

	boolean isActive();

	void onMachineEvent(MachineEvent event);
}
