
package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.init.OverdriveBioticStats;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.entity.player.EntityPlayer;


public class BioticStatAutoShield extends AbstractBioticStat {
    public BioticStatAutoShield(String name, int xp) {
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

    @SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {
		if (event instanceof LivingAttackEvent) {
            DamageSource source = ((LivingAttackEvent) event).getSource();
            if (!OverdriveBioticStats.shield.getShieldState(androidPlayer)) {
                if (OverdriveBioticStats.shield.isDamageValid(source) && event.isCancelable() && OverdriveBioticStats.shield.canActivate(androidPlayer)) {
					event.setResult(Event.Result.DENY);
					event.setCanceled(true);
                    OverdriveBioticStats.shield.setShield(androidPlayer, true);
                } 
			}
		} else if (event instanceof LivingHurtEvent) {
			DamageSource source = ((LivingHurtEvent) event).getSource();
            if (!OverdriveBioticStats.shield.getShieldState(androidPlayer)) {
				if (OverdriveBioticStats.shield.isDamageValid(source) && event.isCancelable() && OverdriveBioticStats.shield.canActivate(androidPlayer)) {
					((LivingHurtEvent) event).setAmount(0F);
					event.setResult(Event.Result.DENY);
					event.setCanceled(true);
					OverdriveBioticStats.shield.setShield(androidPlayer, true);
				}
			}
		}
	}
    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android, level) && android.getEnergyStored() > 0;
    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        return null;
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