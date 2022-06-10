
package matteroverdrive.data.biostats;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import matteroverdrive.data.MOAttributeModifier;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BioticStatSpeed extends AbstractBioticStat {
	private final UUID modifierID;

	public BioticStatSpeed(String name, int xp) {
		super(name, xp);
		setMaxLevel(4);
		modifierID = UUID.fromString("d13345c8-14f7-48fd-bc52-c787c9857a6c");
	}

	@Override
	public void onAndroidUpdate(AndroidPlayer android, int level) {

	}

	private float getSpeedModify(int level) {
		return level * 0.1f;
	}

	public String getDetails(int level) {
		return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.GREEN
				+ Integer.toString(Math.round(getSpeedModify(level) * 100f)) + "%" + TextFormatting.GRAY);
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
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
				new MOAttributeModifier(modifierID, "Android Speed", getSpeedModify(level), 2).setSaved(false));
		return multimap;
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level) {
		return super.isEnabled(android, level) && android.getEnergyStored() > 0;
	}

	@Override
	public boolean isActive(AndroidPlayer androidPlayer, int level) {
		return false;
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level) {
		return 0;
	}
}
