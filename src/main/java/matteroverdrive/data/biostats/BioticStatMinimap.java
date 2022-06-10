
package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;

import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BioticStatMinimap extends AbstractBioticStat {
	public BioticStatMinimap(String name, int xp) {
		super(name, xp);
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level) {

	}

	@Override
	public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {

	}

	@Override
	public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

	}

	@Override
	public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

	}

	@Override
	public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

	}

	@Override
	public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
		return null;
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level) {
		return super.isEnabled(android, level) && android.getEnergyStored() > 0;
	}

	@Override
	public boolean isActive(AndroidPlayer androidPlayer, int level) {
		return true;
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level) {
		return 0;
	}
}
