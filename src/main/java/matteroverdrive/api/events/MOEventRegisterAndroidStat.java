
package matteroverdrive.api.events;

import matteroverdrive.api.android.IBioticStat;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 7/21/2015. Triggered when an Android Ability is
 * registered. When canceled, the ability will not be registered.
 */
public class MOEventRegisterAndroidStat extends Event {
	/**
	 * The Bionic Stat to be registered
	 */
	public final IBioticStat stat;

	public MOEventRegisterAndroidStat(IBioticStat stat) {
		this.stat = stat;
	}

	public boolean isCancelable() {
		return true;
	}
}
