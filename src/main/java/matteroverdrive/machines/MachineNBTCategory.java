
package matteroverdrive.machines;

import java.util.EnumSet;

public enum MachineNBTCategory {
    DATA,
    CONFIGS,
    INVENTORY,
    COLOR,
    GUI;

    public static final EnumSet<MachineNBTCategory> ALL_OPTS = EnumSet.allOf(MachineNBTCategory.class);

    public static int encode(EnumSet<MachineNBTCategory> set) {
        int ret = 0;

        for (MachineNBTCategory val : set) {
            ret |= 1 << val.ordinal();
        }

        return ret;
    }

    public static EnumSet<MachineNBTCategory> decode(int code) {
        MachineNBTCategory[] values = MachineNBTCategory.values();
        EnumSet<MachineNBTCategory> result = EnumSet.noneOf(MachineNBTCategory.class);
        while (code != 0) {
            int ordinal = Integer.numberOfTrailingZeros(code);
            code ^= Integer.lowestOneBit(code);
            result.add(values[ordinal]);
        }
        return result;
    }
}
