
package matteroverdrive.util;

import net.minecraft.world.World;

public class TimeTracker {
    private long lastMark = -9223372036854775808L;

    public TimeTracker() {
    }

    public boolean hasDelayPassed(World world, int time) {
        long worldTime = world.getTotalWorldTime();
        if (worldTime < this.lastMark) {
            this.lastMark = worldTime;
            return false;
        } else if (this.lastMark + (long) time <= worldTime) {
            this.lastMark = worldTime;
            return true;
        } else {
            return false;
        }
    }

    public void markTime(World var1) {
        this.lastMark = var1.getTotalWorldTime();
    }
}
