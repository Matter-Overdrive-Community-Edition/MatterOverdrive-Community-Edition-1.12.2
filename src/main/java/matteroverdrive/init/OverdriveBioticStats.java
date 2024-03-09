
package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.BionicStatGuiInfo;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.data.biostats.*;
import matteroverdrive.handler.ConfigurationHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class OverdriveBioticStats {
	public static BioticStatTeleport teleport;
	public static BiostatNanobots nanobots;
	public static BioticStatNanoArmor nanoArmor;
	public static BioticStatFlotation flotation;
	public static BioticStatSpeed speed;
	public static BioticStatHighJump highJump;
	public static BioticStatEqualizer equalizer;
	public static BioticStatShield shield;
	public static BioticStatAttack attack;
	public static BioticStatCloak cloak;
	public static BioticStatNightVision nightvision;
	public static BioticStatMinimap minimap;
	public static BioticStatFlashCooling flashCooling;
	public static BioticStatShockwave shockwave;
	public static BioticStatAutoShield autoShield;
	public static BioticStatStepAssist stepAssist;
	public static BioticStatZeroCalories zeroCalories;
	public static BioticStatWirelessCharger wirelessCharger;
	public static BioticStatInertialDampers inertialDampers;
	public static BioticStatItemMagnet itemMagnet;
	public static BioticStatAirDash airDash;
	public static BioticstatOxygen oxygen;

	public static void init() {
		teleport = new BioticStatTeleport("teleport", 48);
		nanobots = new BiostatNanobots("nanobots", 26);
		nanoArmor = new BioticStatNanoArmor("nano_armor", 30);
		flotation = new BioticStatFlotation("floatation", 14);
		speed = new BioticStatSpeed("speed", 18);
		highJump = new BioticStatHighJump("high_jump", 36);
		highJump.addReqiredItm(new ItemStack(Blocks.PISTON));
		equalizer = new BioticStatEqualizer("equalizer", 24);
		equalizer.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.spacetime_equalizer));
		shield = new BioticStatShield("shield", 36);
		attack = new BioticStatAttack("attack", 30);
		cloak = new BioticStatCloak("cloak", 36);
		nightvision = new BioticStatNightVision("nightvision", 28);
		minimap = new BioticStatMinimap("minimap", 18);
		flashCooling = new BioticStatFlashCooling("flash_cooling", 28);
		shockwave = new BioticStatShockwave("shockwave", 32);
		autoShield = new BioticStatAutoShield("auto_shield", 26);
		stepAssist = new BioticStatStepAssist("step_assist", 24);
		zeroCalories = new BioticStatZeroCalories("zero_calories", 18);
		wirelessCharger = new BioticStatWirelessCharger("wireless_charger", 32);
		inertialDampers = new BioticStatInertialDampers("inertial_dampers", 18);
		itemMagnet = new BioticStatItemMagnet("item_magnet", 24);
		airDash = new BioticStatAirDash("air_dash", 28);
		oxygen = new BioticstatOxygen("oxygen", 12);

		configure();
	}

	private static void configure() {
		teleport.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.h_compensator));
		teleport.addToEnabledBlacklist(shield);
		nanoArmor.setRoot(nanobots, false);
		nanoArmor.addCompetitor(attack);
		highJump.setRoot(speed, false);
		highJump.addToEnabledBlacklist(shield);
		inertialDampers.setRoot(highJump, false);
		equalizer.setRoot(inertialDampers, false);
		shield.setRoot(nanoArmor, true);
		shield.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.forceFieldEmitter, 1));
		attack.addCompetitor(nanoArmor);
		attack.setRoot(nanobots, false);
		cloak.setRoot(shield, true);
		minimap.addReqiredItm(new ItemStack(Items.COMPASS));
		flashCooling.setRoot(attack, true);
		shockwave.setRoot(flashCooling, true);
		autoShield.setRoot(shield, true);
		oxygen.setRoot(zeroCalories, true);
		flotation.setRoot(oxygen, true);
		itemMagnet.setRoot(stepAssist, false);
		airDash.setRoot(highJump, true);

		int stepSizeX = 52;
		int stepSizeY = 30;

		wirelessCharger.setGuiInfo(new BionicStatGuiInfo(stepSizeX * -1, stepSizeY * 2));
		teleport.setGuiInfo(new BionicStatGuiInfo(0, stepSizeY * -2));

		zeroCalories.setGuiInfo(new BionicStatGuiInfo(stepSizeX, 0));
		oxygen.setGuiInfo(new BionicStatGuiInfo(stepSizeX, stepSizeY * 2, EnumFacing.UP, true));
		flotation.setGuiInfo(new BionicStatGuiInfo(stepSizeX, stepSizeY * 3, EnumFacing.UP));

		nightvision.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 2, stepSizeY * -2));

		nanobots.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, 0));

		nanoArmor.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * 2, EnumFacing.UP, true));
		shield.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * 3, EnumFacing.UP));
		cloak.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * 4, EnumFacing.UP));
		autoShield.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3 - 30, stepSizeY * 3, EnumFacing.EAST));

		attack.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * -2, EnumFacing.DOWN, true));
		flashCooling.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * -3, EnumFacing.DOWN));
		shockwave.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * -4, EnumFacing.DOWN));

		minimap.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 4, stepSizeY * 2));

		speed.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, 0));
		highJump.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, stepSizeY * -2, EnumFacing.DOWN, true));
		airDash.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5 + 30, stepSizeY * -2, EnumFacing.WEST, false));
		inertialDampers.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, stepSizeY * -3, EnumFacing.DOWN));
		equalizer.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, stepSizeY * -4, EnumFacing.DOWN));

		stepAssist.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 6, stepSizeY * 2));
		itemMagnet.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 6, stepSizeY * 3, EnumFacing.UP));
	}

	public static void registerAll(ConfigurationHandler configurationHandler,
			IAndroidStatRegistry androidStatRegistry) {
		androidStatRegistry.registerStat(teleport);
		androidStatRegistry.registerStat(nanobots);
		androidStatRegistry.registerStat(nanoArmor);
		androidStatRegistry.registerStat(flotation);
		androidStatRegistry.registerStat(speed);
		androidStatRegistry.registerStat(highJump);
		androidStatRegistry.registerStat(equalizer);
		androidStatRegistry.registerStat(shield);
		androidStatRegistry.registerStat(attack);
		androidStatRegistry.registerStat(cloak);
		androidStatRegistry.registerStat(nightvision);
		androidStatRegistry.registerStat(minimap);
		androidStatRegistry.registerStat(flashCooling);
		androidStatRegistry.registerStat(shockwave);
		androidStatRegistry.registerStat(autoShield);
		androidStatRegistry.registerStat(stepAssist);
		androidStatRegistry.registerStat(zeroCalories);
		androidStatRegistry.registerStat(wirelessCharger);
		androidStatRegistry.registerStat(inertialDampers);
		androidStatRegistry.registerStat(itemMagnet);
		androidStatRegistry.registerStat(airDash);
		androidStatRegistry.registerStat(oxygen);

		configurationHandler.subscribe(teleport);
		configurationHandler.subscribe(shield);
		configurationHandler.subscribe(nanobots);
		configurationHandler.subscribe(cloak);
		configurationHandler.subscribe(highJump);
		configurationHandler.subscribe(nightvision);
	}
}
