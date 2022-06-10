
package matteroverdrive.data;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.items.includes.EnergyContainer;
import matteroverdrive.tile.MOTileEntityMachineEnergy;

public class MachineEnergyStorage<T extends MOTileEntityMachineEnergy> extends EnergyContainer {
	protected final T machine;

	public MachineEnergyStorage(int capacity, T machine) {
		super(capacity);
		this.machine = machine;
	}

	public MachineEnergyStorage(int capacity, int maxTransfer, T machine) {
		super(capacity, maxTransfer);
		this.machine = machine;
	}

	public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, T machine) {
		super(capacity, maxReceive, maxExtract);
		this.machine = machine;
	}

	public MachineEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, T machine) {
		super(capacity, maxReceive, maxExtract, energy);
		this.machine = machine;
	}

	public void setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
	}

	public int modifyEnergyStored(int amount) {
		if (amount > 0) {
			return receiveEnergy(amount, false);
		} else if (amount < 0) {
			// We need to extract a positive amount, instead of negative.
			return extractEnergy(-amount, false);
		} else {
			return 0;
		}
	}

	@Override
	public int receiveEnergy(int amount, boolean simulate) {
		int ex = super.receiveEnergy(amount, simulate);
		if (ex != 0 && !simulate)
			machine.UpdateClientPower();
		return ex;
	}

	@Override
	public int extractEnergy(int amount, boolean simulate) {
		int ex = super.extractEnergy(amount, simulate);
		if (ex != 0 && !simulate)
			machine.UpdateClientPower();
		return ex;
	}

	public long getInputRate() {
		return (long) (super.maxReceive * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer));
	}

	public long getOutputRate() {
		return (long) (super.maxExtract * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer));
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) (super.getMaxEnergyStored() * machine.getUpgradeMultiply(UpgradeTypes.PowerStorage));
	}

	public int getMaxEnergyStoredUnaltered() {
		return super.getMaxEnergyStored();
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}