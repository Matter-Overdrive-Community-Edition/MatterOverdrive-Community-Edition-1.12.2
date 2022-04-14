
package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.data.quest.*;
import matteroverdrive.data.quest.logic.*;
import matteroverdrive.data.quest.rewards.ItemStackReward;
import matteroverdrive.handler.quest.Quests;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;

public class MatterOverdriveQuests {
    public static final List<WeightedRandom.Item> contractGeneration = new ArrayList<>();
    public static RandomQuestText killAndroids;
    public static GenericQuest cocktailOfAscension;
    public static GenericQuest sacrifice;
    public static GenericQuest weaponsOfWar;
    public static GenericQuest oneTrueLove;
    public static GenericQuest toThePowerOf;
    public static GenericQuest punyHumans;
    public static GenericQuest is_it_really_me;
    public static GenericQuest beast_belly;
    public static GenericQuest crashLanding;
    public static GenericQuest weMustKnow;
    public static GenericMultiQuest gmo;
    public static GenericMultiQuest trade_route;
	public static GenericMultiQuest departmentofagriculture;

    public static void init() {
        initMatterOverdriveQuests();
        initModdedQuests();
    }

    private static void initMatterOverdriveQuests() {
        cocktailOfAscension = (GenericQuest) new GenericQuest(new QuestLogicCocktailOfAscension(), "cocktail_of_ascension", 512).addQuestRewards(new ItemStackReward(MatterOverdrive.ITEMS.androidPill, 1, 0), new ItemStackReward(MatterOverdrive.ITEMS.androidPill, 1, 1), new ItemStackReward(MatterOverdrive.ITEMS.androidPill, 1, 2));
        punyHumans = (GenericQuest) new GenericQuest(new QuestLogicBecomeAndroid(), "puny_humans", 256).addQuestRewards(new ItemStackReward(MatterOverdrive.ITEMS.battery), new ItemStackReward(MatterOverdrive.ITEMS.androidPill, 1, 1), new ItemStackReward(MatterOverdrive.ITEMS.androidPill, 5, 2));
        trade_route = new GenericMultiQuest(new IQuestLogic[]{new QuestLogicBlockInteract(null, true, false), new QuestLogicItemInteract(new QuestItem(new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit).setStackDisplayName("Trade Route Agreement")), true), new QuestLogicConversation("mo.mad_scientist", MatterOverdriveDialogs.tradeRouteQuest, MatterOverdriveDialogs.tradeRouteQuest)}, "trade_route", 180).setAutoComplete(true).setSequential(true);
    }

    private static void initModdedQuests() {
/*        toThePowerOf = (GenericQuest) new GenericQuest(new QuestLogicCraft(new QuestItem[]{
            new QuestItem("BigReactors:BRReactorPart", "BigReactors"),
            new QuestItem("ExtraUtilities:generator", "ExtraUtilities", 1, 1),
            new QuestItem(new ItemStack(MatterOverdrive.ITEMS.battery))
			}, 1, 1, 120).setRandomItem(false).setAutoComplete(true), "to_the_power_of", 0).addQuestRewards(new ItemStackReward(MatterOverdrive.ITEMS.tritanium_ingot, 10), new ItemStackReward(MatterOverdrive.ITEMS.tritanium_plate, 4));
*/
    }

    public static void register(Quests quests) {
        registerMatterOverdriveQuests(quests);
        registerModdedQuests(quests);
    }

    private static void registerMatterOverdriveQuests(Quests quests) {
        quests.registerQuest("cocktail_of_ascension", cocktailOfAscension);
        quests.registerQuest("puny_humans", punyHumans);
//      contractGeneration.add(new WeightedRandomQuest(oneTrueLove,100));
//      contractGeneration.add(new WeightedRandomQuest(is_it_really_me,80));
//      contractGeneration.add(new WeightedRandomQuest(beast_belly,60));
    }

    private static void registerModdedQuests(Quests quests) {
//        quests.registerQuest("to_the_power_of", toThePowerOf);
    }
}
