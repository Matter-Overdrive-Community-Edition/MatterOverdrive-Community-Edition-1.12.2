
package matteroverdrive.api.events;

import matteroverdrive.api.matter.IMatterEntry;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 7/21/2015.
 * Triggered when registering matter entries in the Matter Registry.
 * When canceled, the entry will not be registered.
 * This is a good in-game way to remove matter for items, even when the configs have custom values.
 */
public class MOEventRegisterMatterEntry extends Event {
    /**
     * The matter entry being registered.
     */
    public final IMatterEntry entry;

    public MOEventRegisterMatterEntry(IMatterEntry entry) {
        this.entry = entry;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
