
package matteroverdrive.machines;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.machines.IUpgradeHandler;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 8/23/2015.
 * This Implementation of the {@link IUpgradeHandler} handles Machine stat that need ot have a clamped value by clamping to that value.
 */
public class UpgradeHandlerGeneric implements IUpgradeHandler {
    final Map<UpgradeTypes, Double> mins;
    final Map<UpgradeTypes, Double> max;
    double totalMinimum;
    double totamMaximum;

    public UpgradeHandlerGeneric(double totalMinimum, double totalMaximum) {
        mins = new HashMap<>();
        max = new HashMap<>();
        this.totalMinimum = totalMinimum;
        this.totamMaximum = totalMaximum;
    }

    public UpgradeHandlerGeneric addUpgradeMinimum(UpgradeTypes type, double minimum) {
        mins.put(type, minimum);
        return this;
    }

    public UpgradeHandlerGeneric addUpgradeMaximum(UpgradeTypes type, double maximum) {
        max.put(type, maximum);
        return this;
    }

    @Override
    public double affectUpgrade(UpgradeTypes type, double multiply) {
        if (mins.containsKey(type)) {
            multiply = Math.max(multiply, mins.get(type));
        }
        if (max.containsKey(type)) {
            multiply = Math.min(multiply, max.get(type));
        }
        return MathHelper.clamp(multiply, totalMinimum, totamMaximum);
    }
}
