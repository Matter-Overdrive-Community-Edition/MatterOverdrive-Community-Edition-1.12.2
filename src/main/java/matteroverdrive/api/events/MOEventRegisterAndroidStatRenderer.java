
package matteroverdrive.api.events;

import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 7/24/2015.
 * Triggered by special Bionic Stats that have custom renderers, such as the Shield ability.
 */
public class MOEventRegisterAndroidStatRenderer extends Event {
    /**
     * The type of bionic stat that is being rendered.
     */
    public final Class<? extends IBioticStat> statClass;
    /**
     * The Bionic Stat renderer itself.
     */
    public final IBioticStatRenderer renderer;

    public MOEventRegisterAndroidStatRenderer(Class<? extends IBioticStat> statClass, IBioticStatRenderer renderer) {
        this.statClass = statClass;
        this.renderer = renderer;
    }

    public boolean isCancelable() {
        return true;
    }
}
