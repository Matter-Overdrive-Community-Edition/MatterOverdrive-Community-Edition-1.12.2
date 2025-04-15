
package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.data.biostats.*;
import matteroverdrive.handler.ConfigurationHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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
		equalizer = new BioticStatEqualizer("equalizer", 24);
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
		nanobots.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.me_conversion_matrix, 1));
		nanoArmor.setRoot(nanobots);
		nanoArmor.addCompetitor(attack);
		flotation.addReqiredItm(new ItemStack(Items.GHAST_TEAR, 2));
		nightvision.addReqiredItm(new ItemStack(Items.SPIDER_EYE, 2));
		nanoArmor.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.androidParts, 1));
		nanoArmor.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.androidParts, 2, 1));
		nanoArmor.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.androidParts, 1, 3));
		nanoArmor.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.androidParts, 2, 2));
		highJump.setRoot(speed);
		highJump.addToEnabledBlacklist(shield);
		highJump.addReqiredItm(new ItemStack(Blocks.PISTON));
		highJump.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 4));
		inertialDampers.setRoot(highJump);
		inertialDampers.addReqiredItm(new ItemStack(Blocks.SLIME_BLOCK, 2));
		equalizer.setRoot(inertialDampers);
		equalizer.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.spacetime_equalizer));
		shield.setRoot(nanoArmor);
		shield.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.forceFieldEmitter, 1));
		attack.addCompetitor(nanoArmor);
		attack.setRoot(nanobots);
		cloak.setRoot(shield);
		cloak.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.forceFieldEmitter, 4));
		minimap.addReqiredItm(new ItemStack(Items.COMPASS));
		flashCooling.setRoot(attack);
		flashCooling.addReqiredItm(new ItemStack(Items.WATER_BUCKET));
		shockwave.setRoot(flashCooling);
		shockwave.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 5));
		autoShield.setRoot(shield);
		oxygen.setRoot(zeroCalories);
		airDash.setRoot(highJump);
		itemMagnet.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.s_magnet, 1));
		wirelessCharger.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 1, 2));
		speed.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.item_upgrade, 2, 1));
		stepAssist.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.androidParts, 2, 2));
		stepAssist.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.tritaniumSpine, 1));
		airDash.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.androidParts, 2, 2));
		autoShield.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.forceFieldEmitter, 4));
		attack.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.energyPack, 16));
		zeroCalories.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.emergency_ration, 64));
		oxygen.addReqiredItm(new ItemStack(MatterOverdrive.ITEMS.artifact, 1));
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
