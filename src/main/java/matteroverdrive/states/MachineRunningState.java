package matteroverdrive.states;

import net.minecraft.util.IStringSerializable;

public enum MachineRunningState implements IStringSerializable {
    OFF("off"),
    ON("on");

    private final String name;

    MachineRunningState(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
