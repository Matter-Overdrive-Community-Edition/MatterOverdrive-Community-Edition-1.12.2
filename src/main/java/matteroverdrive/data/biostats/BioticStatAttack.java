
package matteroverdrive.data.biostats;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import matteroverdrive.data.MOAttributeModifier;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.text.DecimalFormat;
import java.util.UUID;

public class BioticStatAttack extends AbstractBioticStat {
	private final UUID modifierID = UUID.fromString("caf3f2ba-75f5-4f2f-84b9-ddfab1fcef25");

	public BioticStatAttack(String name, int xp) {
		super(name, xp);
		setMaxLevel(4);
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
	public boolean isEnabled(AndroidPlayer android, int level) {
		return super.isEnabled(android, level) && android.getEnergyStored() > 0;
	}

	@Override
	public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
				new MOAttributeModifier(modifierID, "Android Attack Damage", getAttackPower(level), 1).setSaved(false));
		return multimap;
	}

	@Override
	public boolean isActive(AndroidPlayer androidPlayer, int level) {
		return false;
	}

	@Override
	public int getDelay(AndroidPlayer androidPlayer, int level) {
		return 0;
	}

	@Override
	public String getDetails(int level) {
		return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.GREEN
				+ DecimalFormat.getPercentInstance().format(getAttackPower(level)) + TextFormatting.GRAY);
	}

	private float getAttackPower(int level) {
		return (level + 1) * 0.05f;
	}
}
