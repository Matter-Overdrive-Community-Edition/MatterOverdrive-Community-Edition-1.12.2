
package matteroverdrive.machines;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MachineComponentAbstract<T extends MOTileEntityMachine> implements IMachineComponent {
	protected final T machine;

	public MachineComponentAbstract(T machine) {
		this.machine = machine;
	}

	public T getMachine() {
		return machine;
	}

	public World getWorld() {
		return machine.getWorld();
	}

	public BlockPos getPos() {
		return machine.getPos();
	}
}
