
package matteroverdrive.api.events.bionicStats;

import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Simeon on 7/21/2015. Triggered by most {@link IBioticStat}. For
 * example the {@link matteroverdrive.data.biostats.BioticStatTeleport} triggers
 * the event when used by the player.
 */
public class MOEventBionicStat extends PlayerEvent {
	/**
	 * The android player using the ability.
	 */
	public final AndroidPlayer android;
	/**
	 * The Ability itself.
	 */
	public final IBioticStat stat;
	/**
	 * The level of the ability being used.
	 */
	public final int level;

	public MOEventBionicStat(IBioticStat stat, int level, AndroidPlayer android) {
		super(android.getPlayer());
		this.android = android;
		this.stat = stat;
		this.level = level;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
