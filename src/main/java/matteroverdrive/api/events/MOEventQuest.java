
package matteroverdrive.api.events;

import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

public class MOEventQuest extends PlayerEvent {
    public final QuestStack questStack;

    public MOEventQuest(QuestStack questStack, EntityPlayer entityPlayer) {
        super(entityPlayer);
        this.questStack = questStack;
    }

    public static class Completed extends MOEventQuest {
        public int xp;
        public List<IQuestReward> rewards;

        public Completed(QuestStack questStack, EntityPlayer entityPlayer, int xp, List<IQuestReward> rewards) {
            super(questStack, entityPlayer);
            this.xp = xp;
            this.rewards = rewards;
        }

        public boolean isCancelable() {
            return true;
        }
    }

    public static class Added extends MOEventQuest {
        public Added(QuestStack questStack, EntityPlayer entityPlayer) {
            super(questStack, entityPlayer);
        }

        public boolean isCancelable() {
            return true;
        }
    }
}
