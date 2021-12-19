
package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageQuestOnObjectivesCompleted extends DialogMessage {
    QuestStack questStack;
    int[] completedObjectives;

    public DialogMessageQuestOnObjectivesCompleted(JsonObject object) {
        super(object);
        this.questStack = questStack;
        this.completedObjectives = completedObjectives;
    }

    public DialogMessageQuestOnObjectivesCompleted(String message, QuestStack questStack, int[] completedObjectives) {
        super(message);
        this.questStack = questStack;
        this.completedObjectives = completedObjectives;
    }

    public DialogMessageQuestOnObjectivesCompleted(String message, String question, QuestStack questStack, int[] completedObjectives) {
        super(message, question);
        this.questStack = questStack;
        this.completedObjectives = completedObjectives;
    }

    public DialogMessageQuestOnObjectivesCompleted(String[] messages, String[] questions, QuestStack questStack, int[] completedObjectives) {
        super(messages, questions);
        this.questStack = questStack;
        this.completedObjectives = completedObjectives;
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(player);
        if (extendedProperties != null) {
            for (QuestStack questStack : extendedProperties.getQuestData().getActiveQuests()) {
                if (questStack.getQuest().areQuestStacksEqual(questStack, this.questStack)) {
                    for (int completedObjective : completedObjectives) {
                        if (!questStack.isObjectiveCompleted(player, completedObjective)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void setQuest(QuestStack questStack) {
        this.questStack = questStack;
    }

    public void setCompletedObjectives(int[] completedObjectives) {
        this.completedObjectives = completedObjectives;
    }
}
