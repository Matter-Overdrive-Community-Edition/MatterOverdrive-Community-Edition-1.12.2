
package matteroverdrive.util;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.quest.IQuest;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class QuestFactory {
	public QuestStack generateQuestStack(Random random, IQuest quest) {
		QuestStack questStack = new QuestStack(quest);
		quest.initQuestStack(random, questStack);
		return questStack;
	}

	@SideOnly(Side.CLIENT)
	public String getFormattedQuestObjective(EntityPlayer entityPlayer, QuestStack questStack, int objectiveInex) {
		boolean isCompleted = questStack.isObjectiveCompleted(entityPlayer, objectiveInex);
		if (isCompleted) {
			// completed
			return TextFormatting.GREEN + Reference.UNICODE_COMPLETED_OBJECTIVE + " "
					+ questStack.getObjective(entityPlayer, objectiveInex);
		} else {
			// not completed
			return TextFormatting.DARK_GREEN + Reference.UNICODE_UNCOMPLETED_OBJECTIVE + " "
					+ questStack.getObjective(entityPlayer, objectiveInex);
		}
	}

	@SideOnly(Side.CLIENT)
	public List<String> getFormattedQuestObjective(EntityPlayer entityPlayer, QuestStack questStack, int objectiveInex,
			int length) {
		return getFormattedQuestObjective(entityPlayer, questStack, objectiveInex, length,
				TextFormatting.DARK_GREEN.toString(), TextFormatting.GREEN.toString());
	}

	@SideOnly(Side.CLIENT)
	public List<String> getFormattedQuestObjective(EntityPlayer entityPlayer, QuestStack questStack, int objectiveInex,
			int length, String uncompletedPrefix, String completedPrefix) {
		List<String> objectiveLines = Minecraft.getMinecraft().fontRenderer
				.listFormattedStringToWidth(questStack.getObjective(entityPlayer, objectiveInex), length);
		boolean isObjectiveComplete = questStack.isObjectiveCompleted(Minecraft.getMinecraft().player, objectiveInex);
		for (int o = 0; o < objectiveLines.size(); o++) {
			String line = "";
			if (isObjectiveComplete) {
				line += completedPrefix;
				if (o == 0) {
					line += Reference.UNICODE_COMPLETED_OBJECTIVE + " ";
				}
			} else {
				line += uncompletedPrefix;
				if (o == 0) {
					line += Reference.UNICODE_UNCOMPLETED_OBJECTIVE + " ";
				}
			}

			line += objectiveLines.get(o);
			objectiveLines.set(o, line);
		}
		return objectiveLines;
	}

	public QuestStack generateQuestStack(String questName) {
		IQuest quest = MatterOverdrive.QUESTS.getQuestByName(questName);
		if (quest != null) {
			QuestStack questStack = new QuestStack(quest);
			quest.initQuestStack(MatterOverdrive.QUESTS.random, questStack);
			return questStack;
		}
		return null;
	}
}
