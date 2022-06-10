
package matteroverdrive.machines.analyzer;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.blocks.BlockMatterAnalyzer;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.matter_network.components.TaskQueueComponent;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

import java.util.EnumSet;

public class ComponentTaskProcessingAnalyzer extends
		TaskQueueComponent<MatterNetworkTaskStorePattern, TileEntityMachineMatterAnalyzer> implements ITickable {
	public static final int PROGRESS_AMOUNT_PER_ITEM = 20;
	public static final int ANALYZE_SPEED = 800;
	public static final int ENERGY_DRAIN_PER_ITEM = 64000;
	public int analyzeTime;
	private boolean isAnalyzing;

	public ComponentTaskProcessingAnalyzer(String name, TileEntityMachineMatterAnalyzer machine, int taskQueueCapacity,
			int queueId) {
		super(name, machine, taskQueueCapacity, queueId);
	}

	@Override
	public void update() {
		if (!getWorld().isRemote) {
			manageAnalyze();
		}
	}

	private void manageAnalyze() {
		isAnalyzing = false;

		if (machine.getRedstoneActive() && !machine.getStackInSlot(machine.input_slot).isEmpty()
				&& machine.getEnergyStorage().getEnergyStored() > 0) {
			if (getTaskQueue().remaintingCapacity() > 0
					&& !networkHasPattern(machine.getStackInSlot(machine.input_slot))) {
				isAnalyzing = true;
			}
		}

		if (isAnalyzing() && hasEnoughPower()) {
			machine.getEnergyStorage().extractEnergy(getEnergyDrainPerTick(), false);
			machine.UpdateClientPower();

			if (analyzeTime < getSpeed()) {
				analyzeTime++;
			} else {
				analyzeItem();
				analyzeTime = 0;
			}
		}

		if (!isAnalyzing()) {
			analyzeTime = 0;
		}

		// Update the machine's state with the running state.
		BlockMatterAnalyzer.setState(isAnalyzing, this.getWorld(), this.getPos());
	}

	public void analyzeItem() {
		ItemStack itemStack = machine.getStackInSlot(machine.input_slot).copy();
		itemStack.setCount(1);
		MatterNetworkTaskStorePattern storePattern = new MatterNetworkTaskStorePattern(itemStack,
				PROGRESS_AMOUNT_PER_ITEM);
		storePattern.setState(MatterNetworkTaskState.WAITING);
		if (machine.getNetwork() != null) {
			machine.getNetwork().post(new IMatterNetworkEvent.Task(storePattern));
		}
		if (storePattern.getState().belowOrEqual(MatterNetworkTaskState.WAITING)) {
			addStorePatternTask(storePattern);
		}

		TileEntity TE = getWorld().getTileEntity(getPos());

		// Make sure at that location we don't have a muffler installed.
		if (TE != null) {
			TileEntityMachineMatterAnalyzer temma = (TileEntityMachineMatterAnalyzer) TE;

			ItemStack stack = temma.getStackInSlot(0);

			if (!(temma.getUpgradeMultiply(UpgradeTypes.Muffler) == 2d || stack.isEmpty())) {
				SoundHandler.PlaySoundAt(getWorld(), MatterOverdriveSounds.scannerSuccess, SoundCategory.BLOCKS,
						getPos().getX(), getPos().getY(), getPos().getZ());
			}
		}

		machine.decrStackSize(machine.input_slot, 1);
		machine.markDirty();
	}

	public boolean networkHasPattern(ItemStack stack) {
		for (IMatterNetworkClient client : machine.getNetwork().getClients()) {
			if (client instanceof IMatterDatabase) {
				ItemPattern hasPattern = ((IMatterDatabase) client).getPattern(stack);
				if (hasPattern != null && hasPattern.getProgress() >= 100) {
					return true;
				}
			}
		}
		return false;
	}

	public void addStorePatternTask(MatterNetworkTaskStorePattern task) {
		if (getTaskQueue().queue(task)) {
			sendTaskQueueAddedToWatchers(task.getId());
		}
	}

	public boolean isAnalyzing() {
		return isAnalyzing;
	}

	public boolean hasEnoughPower() {
		return machine.getEnergyStorage().getEnergyStored() >= getEnergyDrainPerTick();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
		super.readFromNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA)) {
			analyzeTime = nbt.getShort("AnalyzeTime");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
		super.writeToNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA)) {
			nbt.setShort("AnalyzeTime", (short) analyzeTime);
		}
	}

	public int getSpeed() {
		return (int) Math.round(ANALYZE_SPEED * machine.getUpgradeMultiply(UpgradeTypes.Speed));
	}

	public int getEnergyDrainPerTick() {
		return getEnergyDrainMax() / getSpeed();
	}

	public int getEnergyDrainMax() {
		return (int) Math.round(ENERGY_DRAIN_PER_ITEM * machine.getUpgradeMultiply(UpgradeTypes.PowerUsage));
	}

	public float getProgress() {
		return (float) analyzeTime / (float) getSpeed();
	}
}
