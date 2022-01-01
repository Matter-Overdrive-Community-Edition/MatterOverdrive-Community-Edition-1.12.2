package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.*;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.monster.EntityRogueAndroid;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.village.VillageCreatationMadScientist;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;

@Mod.EventBusSubscriber
public class MatterOverdriveEntities {
    public static final int ENTITY_STARTING_ID = 171;
    public static EntityRogueAndroid rogueandroid;

    public static VillagerProfession MAD_SCIENTIST_PROFESSION;
    public static VillagerCareer MAD_SCIENTIST_CAREER;
    public static boolean enableVillager = false;

    public static void init(FMLPreInitializationEvent event, ConfigurationHandler configurationHandler) {
        rogueandroid = new EntityRogueAndroid();
        configurationHandler.subscribe(rogueandroid);
    }

    public static void register(FMLPostInitializationEvent event) {
        MatterOverdrive.CONFIG_HANDLER.config.load();
        int id = 0;
        addEntity(EntityFailedPig.class, "failed_pig", 15771042, 0x33CC33, id++);
        addEntity(EntityFailedCow.class, "failed_cow", 4470310, 0x33CC33, id++);
        addEntity(EntityFailedChicken.class, "failed_chicken", 10592673, 0x33CC33, id++);
        addEntity(EntityFailedSheep.class, "failed_sheep", 15198183, 0x33CC33, id++);
        if (addEntity(EntityVillagerMadScientist.class, "mad_scientist", 0xFFFFFF, 0, id++)) {
            VillageCreatationMadScientist creatationMadScientist = new VillageCreatationMadScientist();
            VillagerRegistry.instance().registerVillageCreationHandler(creatationMadScientist);
        }
        addEntity(EntityMutantScientist.class, "mutant_scientist", 0xFFFFFF, 0x00FF00, id++);
        if (addEntity(EntityMeleeRougeAndroidMob.class, "rogue_android", 0xFFFFF, 0, id++))
            EntityRogueAndroid.addAsBiomeGen(EntityMeleeRougeAndroidMob.class);
        if (addEntity(EntityRangedRogueAndroidMob.class, "ranged_rogue_android", 0xFFFFF, 0, id++))
            EntityRogueAndroid.addAsBiomeGen(EntityRangedRogueAndroidMob.class);
        addEntity(EntityDrone.class, "drone", 0x3e5154, 0xbaa1c4, id++);
        MatterOverdrive.CONFIG_HANDLER.save();
    }

    @SubscribeEvent
    public static void registerProfessions(final RegistryEvent.Register<VillagerProfession> event) {
		final IForgeRegistry<VillagerProfession> registry = event.getRegistry();
        MAD_SCIENTIST_PROFESSION = new VillagerProfession(Reference.MOD_ID + ":mad_scientist",
		Reference.PATH_ENTITIES + "mad_scientist.png",
		Reference.PATH_ENTITIES + "hulking_scinetist.png");
		{
			registry.register(MAD_SCIENTIST_PROFESSION);
			new VillagerCareer(MAD_SCIENTIST_PROFESSION, Reference.MOD_ID + ".mad_scientist.name")
				
			.addTrade(1, new EntityVillager.ListItemForEmeralds(MatterOverdrive.ITEMS.androidPill, new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.androidPill, 1, 1), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.androidPill, 2, 2), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(MatterOverdrive.ITEMS.h_compensator, new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(MatterOverdrive.ITEMS.dilithium_crystal, new EntityVillager.PriceInfo(-5, -7)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(MatterOverdrive.ITEMS.weapon_module_barrel, new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 1), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 2), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 3), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 4), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(new ItemStack(MatterOverdrive.ITEMS.weapon_module_barrel, 1, 5), new EntityVillager.PriceInfo(20, 30)))
			.addTrade(1, new EntityVillager.ListItemForEmeralds(MatterOverdrive.ITEMS.earl_gray_tea, new EntityVillager.PriceInfo(-8, 30)));
		}
    }
    public static boolean addEntity(Class<? extends Entity> enityClass, String name, int mainColor, int spotsColor, int id) {
        boolean enabled = MatterOverdrive.CONFIG_HANDLER.config.getBoolean("enable", String.format("%s.%s", ConfigurationHandler.CATEGORY_ENTITIES, name), true, "");
        if (enabled)
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), enityClass, name, id, MatterOverdrive.INSTANCE, 64, 1, true, mainColor, spotsColor);
        return enabled;
    }
}