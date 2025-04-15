
package matteroverdrive.init;

import com.google.common.base.Preconditions;
import matteroverdrive.items.*;
import matteroverdrive.items.android.RougeAndroidParts;
import matteroverdrive.items.android.TritaniumSpine;
import matteroverdrive.items.armour.TritaniumArmor;
import matteroverdrive.items.food.AndroidPill;
import matteroverdrive.items.food.EarlGrayTea;
import matteroverdrive.items.food.MOItemFood;
import matteroverdrive.items.food.RomulanAle;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.items.includes.MOItemOre;
import matteroverdrive.items.starmap.ItemBuildingBase;
import matteroverdrive.items.starmap.ItemBuildingMatterExtractor;
import matteroverdrive.items.starmap.ItemBuildingPowerGenerator;
import matteroverdrive.items.starmap.ItemBuildingResidential;
import matteroverdrive.items.starmap.ItemBuildingShipHangar;
import matteroverdrive.items.starmap.ItemColonizerShip;
import matteroverdrive.items.starmap.ItemScoutShip;
import matteroverdrive.items.starmap.ShipFactory;
import matteroverdrive.items.tools.*;
import matteroverdrive.items.weapon.*;
import matteroverdrive.items.weapon.module.*;
import matteroverdrive.util.MOLog;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class MatterOverdriveItems {
	public final static Item.ToolMaterial TOOL_MATERIAL_TRITANIUM = EnumHelper.addToolMaterial("TRITANIUM", 2, 3122, 6f,
			2f, 14);
	public final static ItemArmor.ArmorMaterial ARMOR_MATERIAL_TRITANIUM = EnumHelper.addArmorMaterial("TRITANIUM",
			"tritanium", 66, new int[] { 4, 9, 7, 4 }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0f);
	public static List<Item> items = new ArrayList<>();
	// Materials
	public MOItemOre dilithium_crystal;
	public MOItemOre tritanium_ingot;
	public MOItemOre tritanium_nugget;
	public MOItemOre tritanium_dust;
	public MOBaseItem tritanium_plate;
	public MatterDust matter_dust;
	public MatterDust matter_dust_refined;

	// Food(ish)
	public AndroidPill androidPill;
	public MOItemFood emergency_ration;
	public EarlGrayTea earl_gray_tea;
	public RomulanAle romulan_ale;

	// Storage
	public Battery battery;
	public Battery hc_battery;
	public Battery creative_battery;
	public MatterContainer matterContainer;

	// Crafting
	public MOBaseItem me_conversion_matrix;
	public MOBaseItem h_compensator;
	public MOBaseItem integration_matrix;
	public MOBaseItem machine_casing;
	public MOBaseItem s_magnet;
	public IsolinearCircuit isolinear_circuit;
	public MOBaseItem forceFieldEmitter;
	public MOBaseItem weaponHandle;
	public MOBaseItem weaponReceiver;
	public MOBaseItem plasmaCore;
	public MatterItem matterItem;

	// Ships
	public ItemScoutShip scoutShip;
	public ItemColonizerShip colonizerShip;
    
	// Buildings
	public ShipFactory shipFactory;;
	public ItemBuildingBase buildingBase;
	public ItemBuildingResidential buildingResidential;
	public ItemBuildingMatterExtractor buildingMatterExtractor;
	public ItemBuildingShipHangar buildingShipHangar;
	public ItemBuildingPowerGenerator buildingPowerGenerator;

	// Weapons
	public Phaser phaser;
	public PhaserRifle phaserRifle;
	public PlasmaShotgun plasmaShotgun;
	public IonSniper ionSniper;
	public OmniTool omniTool;

	// Weapon Modules
	public WeaponModuleColor weapon_module_color;
	public WeaponModuleBarrel weapon_module_barrel;
	public WeaponModuleSniperScope sniperScope;
	public WeaponModuleRicochet weaponModuleRicochet;
	public WeaponModuleHoloSights weaponModuleHoloSights;
	public HoloSightsBase holoSightsBase;

	// Tools
	public Wrench wrench;
	public TritaniumAxe tritaniumAxe;
	public TritaniumPickaxe tritaniumPickaxe;
	public TritaniumSword tritaniumSword;
	public TritaniumHoe tritaniumHoe;
	public TritaniumShovel tritaniumShovel;
	public TritaniumArmor tritaniumHelmet;
	public TritaniumArmor tritaniumChestplate;
	public TritaniumArmor tritaniumLeggings;
	public TritaniumArmor tritaniumBoots;

	// Android
	public RougeAndroidParts androidParts;
	public TritaniumSpine tritaniumSpine;

	// Misc
	public MatterScanner matter_scanner;
	public PatternDrive pattern_drive;
	public NetworkFlashDrive networkFlashDrive;
	public ItemUpgrade item_upgrade;
	public TransportFlashDrive transportFlashDrive;
	public EnergyPack energyPack;
	public DataPad dataPad;
	public Contract contract;
	public PortableDecomposer portableDecomposer;
	public SecurityProtocol security_protocol;
	public SpacetimeEqualizer spacetime_equalizer;
	public ItemRecordTransformation recordTransformation;
	public MOBaseItem trilithiumCrystal;
	public MOBaseItem artifact;
	public MOBaseItem quantumFoldManipulator;

	private int registeredCount = 0;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		items.forEach(i -> event.getRegistry().register(i));
	}

	private static void addToDungons(Item item, int min, int max, int chance) {
		/*
		 * ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new
		 * WeightedRandomChestContent(new ItemStack(item),min,max,chance));
		 * ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new
		 * WeightedRandomChestContent(new ItemStack(item),min,max,chance));
		 * ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new
		 * WeightedRandomChestContent(new ItemStack(item),min,max,chance));
		 * ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new
		 * WeightedRandomChestContent(new ItemStack(item),min,max,chance));
		 * ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new
		 * WeightedRandomChestContent(new ItemStack(item),min,max,chance));
		 */
	}

	public void init() {

		MOLog.info("Registering items");

//		Materials
		dilithium_crystal = register(new MOItemOre("dilithium_crystal", "gemDilithium"));
		tritanium_ingot = register(new MOItemOre("tritanium_ingot", "ingotTritanium"));
		tritanium_nugget = register(new MOItemOre("tritanium_nugget", "nuggetTritanium"));
		tritanium_dust = register(new MOItemOre("tritanium_dust", "dustTritanium"));
		tritanium_plate = register(new MOBaseItem("tritanium_plate"));
		matter_dust = register(new MatterDust("matter_dust", "matterDust", false));
		matter_dust_refined = register(new MatterDust("matter_dust_refined", "matterDustRefined", true));

//		Food(ish)
		androidPill = register(new AndroidPill("android_pill"));
		emergency_ration = register(new MOItemFood("emergency_ration", 8, 0.8f, false));
		earl_gray_tea = register(new EarlGrayTea("earl_gray_tea"));
		romulan_ale = register(new RomulanAle("romulan_ale"));

//		Storage
		battery = register(new Battery("battery", 1 << 19, 400, 800));
		hc_battery = register(new Battery("hc_battery", 1 << 20, 4096, 4096));
		creative_battery = register(new CreativeBattery("creative_battery"));
		matterContainer = register(new MatterContainer("matter_container"));

//		Crafting
		me_conversion_matrix = register(new MOBaseItem("me_conversion_matrix"));
		h_compensator = register(new MOBaseItem("h_compensator"));
		integration_matrix = register(new MOBaseItem("integration_matrix"));
		machine_casing = register(new MOBaseItem("machine_casing"));
		s_magnet = register(new MOBaseItem("s_magnet"));
		isolinear_circuit = register(new IsolinearCircuit("isolinear_circuit"));
		matterItem = register(new MatterItem("matter"));
		forceFieldEmitter = register(new MOBaseItem("forcefield_emitter"));
		weaponHandle = register(new MOBaseItem("weapon_handle"));
		weaponReceiver = register(new MOBaseItem("weapon_receiver"));
		plasmaCore = register(new MOBaseItem("plasma_core"));

//		Ships
		scoutShip =  register(new ItemScoutShip("scout_ship"));
		colonizerShip =  register(new ItemColonizerShip("ship_colonizer"));

//		Buildings
		shipFactory =  register(new ShipFactory("ship_factory"));
		buildingBase =  register(new ItemBuildingBase("building_base"));
		buildingMatterExtractor =  register(new ItemBuildingMatterExtractor("building_matter_extractor"));
		buildingResidential =  register(new ItemBuildingResidential("building_residential"));
		buildingShipHangar =  register(new ItemBuildingShipHangar("building_ship_hangar"));
		buildingPowerGenerator =  register(new ItemBuildingPowerGenerator("building_power_generator"));
        
//		Weapons
		phaser = register(new Phaser("phaser"));
		phaserRifle = register(new PhaserRifle("phaser_rifle"));
		plasmaShotgun = register(new PlasmaShotgun("plasma_shotgun"));
		ionSniper = register(new IonSniper("ion_sniper"));
		omniTool = register(new OmniTool("omni_tool"));

//		Weapon Modules
		weapon_module_color = register(new WeaponModuleColor("weapon_module_color"));
		weapon_module_barrel = register(new WeaponModuleBarrel("weapon_module_barrel"));
		sniperScope = register(new WeaponModuleSniperScope("sniper_scope"));
		weaponModuleRicochet = register(new WeaponModuleRicochet("weapon_module_ricochet"));
		weaponModuleHoloSights = register(new WeaponModuleHoloSights("weapon_module_holo_sights"));
		holoSightsBase = register(new HoloSightsBase("holo_sights_base"));

//		Tools
		wrench = register(new Wrench("tritanium_wrench"));
		tritaniumAxe = register(new TritaniumAxe("tritanium_axe"));
		tritaniumPickaxe = register(new TritaniumPickaxe("tritanium_pickaxe"));
		tritaniumSword = register(new TritaniumSword("tritanium_sword"));
		tritaniumHoe = register(new TritaniumHoe("tritanium_hoe"));
		tritaniumShovel = register(new TritaniumShovel("tritanium_shovel"));
		tritaniumHelmet = register(new TritaniumArmor("tritanium_helmet", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.HEAD));
		tritaniumChestplate = register(new TritaniumArmor("tritanium_chestplate", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.CHEST));
		tritaniumLeggings = register(new TritaniumArmor("tritanium_leggings", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.LEGS));
		tritaniumBoots = register(new TritaniumArmor("tritanium_boots", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.FEET));

//		Android
		androidParts = register(new RougeAndroidParts("rogue_android_part"));
		tritaniumSpine = register(new TritaniumSpine("tritanium_spine"));

//		Misc
		matter_scanner = register(new MatterScanner("matter_scanner"));
		pattern_drive = register(new PatternDrive("pattern_drive", 2));
		networkFlashDrive = register(new NetworkFlashDrive("network_flash_drive"));
		item_upgrade = register(new ItemUpgrade("upgrade"));
		transportFlashDrive = register(new TransportFlashDrive("transport_flash_drive"));
		energyPack = register(new EnergyPack("energy_pack"));
		dataPad = register(new DataPad("data_pad"));
		contract = register(new Contract("contract"));
		portableDecomposer = register(new PortableDecomposer("portable_decomposer", 512, 0.1f));
		security_protocol = register(new SecurityProtocol("security_protocol"));
		spacetime_equalizer = register(new SpacetimeEqualizer("spacetime_equalizer"));
		recordTransformation = register(new ItemRecordTransformation());
		artifact = register(new MOBaseItem("artifact"));
		trilithiumCrystal = register(new MOBaseItem("trilithium_crystal"));
		quantumFoldManipulator = register(new MOBaseItem("quantum_fold_manipulator"));
		Preconditions.checkNotNull(TOOL_MATERIAL_TRITANIUM).setRepairItem(new ItemStack(tritanium_ingot));
		Preconditions.checkNotNull(ARMOR_MATERIAL_TRITANIUM).setRepairItem(new ItemStack(tritanium_ingot));
		MOLog.info("Finished registering %d items", registeredCount);
	}

	protected <T extends Item> T register(T item) {
		items.add(item);
		registeredCount++;
		return item;
	}

	public void addToDungons() {
		weapon_module_color.addToDunguns();
//		addToDungons(emergency_ration, 1, 8, 6);
//		addToDungons(earl_gray_tea, 1, 2, 2);
//		addToDungons(romulan_ale, 1, 2, 2);
//		addToDungons(recordTransformation, 1, 2, 1);

		addToMODungons();
	}

	public void addToMODungons() {
		// TODO: Add to dungeon loot
		/*
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(emergency_ration),8,20,100));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(earl_gray_tea),4,10,50));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(romulan_ale),4,10,50));
		 * 
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(isolinear_circuit,0,1,5,50));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(isolinear_circuit,1,1,4,40));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(isolinear_circuit,2,1,3,30));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(isolinear_circuit,3,1,2,20));
		 * 
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(androidPill,1,1,2,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(androidPill,0,1,1,5));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.
		 * DAMAGE_BARREL_ID,1,1,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.
		 * FIRE_BARREL_ID,1,1,8));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.
		 * HEAL_BARREL_ID,1,1,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.
		 * EXPLOSION_BARREL_ID,1,1,5));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(weaponModuleRicochet),1,1,5));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(tritaniumSpine),1,1,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(androidParts,0,1,2,15));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(androidParts,1,1,2,15));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(androidParts,2,1,2,15));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(androidParts,3,1,2,15));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(hc_battery),1,1,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(h_compensator),1,2,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(me_conversion_matrix),1,2,10));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(matterContainerFull),4,8,20));
		 * ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new
		 * WeightedRandomChestContent(new ItemStack(phaser),1,1,10));
		 */
	}
}