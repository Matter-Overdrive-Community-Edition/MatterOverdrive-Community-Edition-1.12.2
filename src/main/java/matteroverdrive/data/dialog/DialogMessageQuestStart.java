
package matteroverdrive.data.dialog;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;

public class DialogMessageQuestStart extends DialogMessage {
    QuestStack questStack;

    public DialogMessageQuestStart() {
        super();
    }

    public DialogMessageQuestStart(QuestStack questStack) {
        super();
        this.questStack = questStack;
    }

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player) {
        OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(player);
        return extendedProperties != null && questStack != null && questStack.getQuest().canBeAccepted(questStack, player);
    }

    public DialogMessageQuestStart setQuest(QuestStack questStack) {
        this.questStack = questStack;
        return this;
    }
}
