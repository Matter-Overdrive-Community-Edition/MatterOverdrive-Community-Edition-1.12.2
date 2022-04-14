
package matteroverdrive.items;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.items.includes.MOBaseItem;

public class HoloSightsBase extends MOBaseItem {
    public HoloSightsBase(String name) {
        super(name);
        this.setMaxStackSize(8);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_MODULES);
    }
}