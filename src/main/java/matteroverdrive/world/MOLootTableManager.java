package matteroverdrive.world;

import matteroverdrive.Reference;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.items.food.AndroidPill;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MOLootTableManager {
	public static ResourceLocation MO_CRASHED_SHIP = new ResourceLocation(Reference.MOD_ID + ":" + "crashed_ship");
		
@SubscribeEvent
public void onLootTablesLoaded(LootTableLoadEvent event) {
 
    if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
 
        final LootPool pool2 = event.getTable().getPool("pool2");
 
        if (pool2 != null) {
 
            // pool2.addEntry(new LootEntryItem(ITEM, WEIGHT, QUALITY, FUNCTIONS, CONDITIONS, NAME));
            pool2.addEntry(new LootEntryItem(MatterOverdrive.ITEMS.androidPill, 10, 0, new LootFunction[0], new LootCondition[0], "loottable:redPill"));
        }
    }
}
}

