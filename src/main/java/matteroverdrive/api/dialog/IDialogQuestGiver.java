
package matteroverdrive.api.dialog;

import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;

public interface IDialogQuestGiver {
    void giveQuest(IDialogMessage message, QuestStack questStack, EntityPlayer entityPlayer);
}
