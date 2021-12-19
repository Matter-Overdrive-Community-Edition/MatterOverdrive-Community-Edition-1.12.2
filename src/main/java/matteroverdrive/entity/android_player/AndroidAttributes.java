
package matteroverdrive.entity.android_player;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class AndroidAttributes {
    public static final IAttribute attributeGlitchTime = new RangedAttribute(null, "android.glitchTime", 1, 0, 1).setDescription("Glitch Time Percent");
    public static final IAttribute attributeBatteryUse = new RangedAttribute(null, "android.batteryUse", 1, 0, Double.MAX_VALUE).setDescription("Battery Use Percent");
}
