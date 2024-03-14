package matteroverdrive.handler;

import matteroverdrive.items.weapon.EnergyPack;
import matteroverdrive.items.weapon.IonSniper;
import matteroverdrive.items.weapon.OmniTool;
import matteroverdrive.items.weapon.Phaser;
import matteroverdrive.items.weapon.PhaserRifle;
import matteroverdrive.items.weapon.PlasmaShotgun;
import matteroverdrive.util.IConfigSubscriber;

public class WeaponConfigHandler implements IConfigSubscriber {

	@Override
	public void onConfigChanged(ConfigurationHandler config) {

		// Energy Pack
		EnergyPack.ENERGYPACKVALUE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ENERGY_PACK_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32000,
				0, 2147483647, "Energy Pack Max value");
		// Phaser
		Phaser.RANGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_RANGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 18, 0, 2147483647,
				"Phaser Range");
		Phaser.MAX_HEAT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_HEAT_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 100, 0, 2147483647,
				"Phaser Heat");
		Phaser.MAX_CAPACITY = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_CAPACITY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32000, 0, 2147483647,
				"Phaser Battery Capacity");
		Phaser.BASE_DAMAGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_DAMAGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 21, 0, 2147483647,
				"Phaser Damage");
		Phaser.BASE_SHOT_COOLDOWN = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_COOLDOWN_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 11, 0, 2147483647,
				"Phaser Shot Cooldown ");
		Phaser.MAX_USE_TIME = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_USE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 60, 0, 2147483647,
				"Phaser Max use time");
		Phaser.ENERGY_PER_SHOT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASER_ENERGY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 3072, 0, 2147483647,
				"Phaser Energy per shot");
		// Phaser Rifle
		PhaserRifle.RANGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_RANGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32, 0, 2147483647,
				"Phaser Rifle Range");
		PhaserRifle.MAX_HEAT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_HEAT_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 80, 0, 2147483647,
				"Phaser Rifle Heat");
		PhaserRifle.MAX_CAPACITY = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_CAPACITY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32000, 0, 2147483647,
				"Phaser Rifle Battery Capacity");
		PhaserRifle.BASE_DAMAGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_DAMAGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 8, 0, 2147483647,
				"Phaser Rifle Damage");
		PhaserRifle.SHOTSPEED = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_SPEED_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 4, 0, 2147483647,
				"Phaser Rifle Shot Speed");
		PhaserRifle.BASE_SHOT_COOLDOWN = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_COOLDOWN_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 11, 0, 2147483647,
				"Phaser Rifle Shot Cooldown ");
		PhaserRifle.MAX_USE_TIME = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_USE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 512, 0, 2147483647,
				"Phaser Rifle Max use time");
		PhaserRifle.ENERGY_PER_SHOT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PHASERRIFLE_ENERGY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 1024, 0, 2147483647,
				"Phaser Rifle Energy per shot");
		// PlasmaShotgun
		PlasmaShotgun.RANGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_RANGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 16, 0, 2147483647,
				"Plasma Shotgun Range");
		PlasmaShotgun.MAX_HEAT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_HEAT_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 80, 0, 2147483647,
				"Plasma Shotgun Heat");
		PlasmaShotgun.MAX_CAPACITY = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_CAPACITY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32000, 0, 2147483647,
				"Plasma Shotgun Battery Capacity");
		PlasmaShotgun.BASE_DAMAGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_DAMAGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 16, 0, 2147483647,
				"Plasma Shotgun Damage");
		PlasmaShotgun.SHOTSPEED = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_SPEED_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 3, 0, 2147483647,
				"Plasma Shotgun Shot Speed");
		PlasmaShotgun.BASE_SHOT_COOLDOWN = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_COOLDOWN_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 22, 0, 2147483647,
				"Plasma Shotgun Shot Cooldown ");
		PlasmaShotgun.MAX_CHARGE_TIME = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_CHARGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 20, 0, 2147483647,
				"Plasma Shotgun Max charge time");
		PlasmaShotgun.ENERGY_PER_SHOT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_PLASMASHOTGUN_ENERGY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 2560, 0, 2147483647,
				"Plasma Shotgun Energy per shot");
		// Ion Sniper
		IonSniper.RANGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_RANGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 96, 0, 2147483647,
				"Ion Sniper Range");
		IonSniper.MAX_HEAT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_HEAT_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 100, 0, 2147483647,
				"Ion Sniper Heat");
		IonSniper.MAX_CAPACITY = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_CAPACITY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32000, 0, 2147483647,
				"Ion Sniper Battery Capacity");
		IonSniper.BASE_DAMAGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_DAMAGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 21, 0, 2147483647,
				"Ion Sniper Damage");
		IonSniper.SHOTSPEED = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_SPEED_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 8, 0, 2147483647,
				"Ion Sniper Shot Speed");
		IonSniper.BASE_SHOT_COOLDOWN = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_COOLDOWN_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 30, 0, 2147483647,
				"Ion Sniper Shot Cooldown ");
		IonSniper.MAX_USE_TIME = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_USE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 512, 0, 2147483647,
				"Ion Sniper Max use time");
		IonSniper.ENERGY_PER_SHOT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_ION_ENERGY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 3072, 0, 2147483647,
				"Ion Sniper Energy per shot");
		// OmniTool
		OmniTool.RANGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_RANGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 24, 0, 2147483647,
				"OmniTool Range");
		OmniTool.MAX_HEAT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_HEAT_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 80, 0, 2147483647,
				"OmniTool Heat");
		OmniTool.MAX_CAPACITY = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_CAPACITY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 32000, 0, 2147483647,
				"OmniTool Battery Capacity");
		OmniTool.BASE_DAMAGE = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_DAMAGE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 7, 0, 2147483647,
				"OmniTool Damage");
		OmniTool.SHOTSPEED = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_SPEED_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 3, 0, 2147483647,
				"OmniTool Shot Speed");
		OmniTool.BASE_SHOT_COOLDOWN = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_COOLDOWN_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 18, 0, 2147483647,
				"OmniTool Shot Cooldown ");
		OmniTool.MAX_USE_TIME = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_USE_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 240, 0, 2147483647,
				"OmniTool Max use time");
		OmniTool.ENERGY_PER_SHOT = config.config.getInt(ConfigurationHandler.KEY_WEAPON_OMNITOOL_ENERGY_VALUE, ConfigurationHandler.CATEGORY_WEAPON, 512, 0, 2147483647,
				"OmniTool Energy per shot");
	}
}