
package matteroverdrive.api.quest;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

public interface IQuestLogic {
	void loadFromJson(JsonObject jsonObject);

	String modifyTitle(QuestStack questStack, String original);

	boolean canAccept(QuestStack questStack, EntityPlayer entityPlayer);

	String modifyInfo(QuestStack questStack, String info);

	boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex);

	String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex);

	int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer, int count);

	void initQuestStack(Random random, QuestStack questStack);

	QuestLogicState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer);

	boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo);

	void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer);

	void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer);

	int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp);

	void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards);

	String getID();
}
