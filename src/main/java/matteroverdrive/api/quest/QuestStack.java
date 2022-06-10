
package matteroverdrive.api.quest;

import java.util.List;
import java.util.UUID;

import matteroverdrive.MatterOverdrive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class QuestStack {
	boolean completed;
	private NBTTagCompound tagCompound;
	private UUID giverUniqueID;
	private Entity giver;
	private IQuest quest;

	QuestStack() {
	}

	public QuestStack(IQuest quest, Entity giver) {
		this.quest = quest;
		if (giver != null) {
			this.giverUniqueID = giver.getUniqueID();
		}
		this.giver = giver;
	}

	public QuestStack(IQuest quest) {
		this.quest = quest;
	}

	public static QuestStack loadFromNBT(NBTTagCompound tagCompound) {
		if (tagCompound != null) {
			QuestStack questStack = new QuestStack();
			questStack.readFromNBT(tagCompound);
			return questStack;
		}
		return null;
	}

	public static boolean canComplete(EntityPlayer entityPlayer, QuestStack questStack) {
		for (int i = 0; i < questStack.getObjectivesCount(entityPlayer); i++) {
			if (!questStack.isObjectiveCompleted(entityPlayer, i)) {
				return false;
			}
		}
		return true;
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		if (this.tagCompound != null) {
			tagCompound.setTag("Data", this.tagCompound);
		}
		if (giverUniqueID != null) {
			tagCompound.setLong("giveIdLow", giverUniqueID.getLeastSignificantBits());
			tagCompound.setLong("giveIdHigh", giverUniqueID.getMostSignificantBits());
		}
		tagCompound.setShort("Quest", (short) MatterOverdrive.QUESTS.getQuestID(quest));
		tagCompound.setBoolean("Completed", completed);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		if (tagCompound.hasKey("Data", Constants.NBT.TAG_COMPOUND)) {
			this.tagCompound = tagCompound.getCompoundTag("Data");
		}
		if (tagCompound.hasKey("giveIdLow", Constants.NBT.TAG_LONG)
				&& tagCompound.hasKey("giveIdHigh", Constants.NBT.TAG_LONG)) {
			giverUniqueID = new UUID(tagCompound.getLong("giveIdLow"), tagCompound.getLong("giveIdHigh"));
		}
		if (tagCompound.hasKey("Quest", Constants.NBT.TAG_SHORT)) {
			quest = MatterOverdrive.QUESTS.getQuestWithID(tagCompound.getShort("Quest"));
		}
		completed = tagCompound.getBoolean("Completed");
	}

	public String getTitle() {
		return quest.getTitle(this);
	}

	public int getXP(EntityPlayer entityPlayer) {
		return quest.getXpReward(this, entityPlayer);
	}

	public String getTitle(EntityPlayer entityPlayer) {
		return quest.getTitle(this, entityPlayer);
	}

	public String getInfo(EntityPlayer entityPlayer) {
		return quest.getInfo(this, entityPlayer);
	}

	public String getObjective(EntityPlayer entityPlayer, int objectiveIndex) {
		return quest.getObjective(this, entityPlayer, objectiveIndex);
	}

	public int getObjectivesCount(EntityPlayer entityPlayer) {
		return quest.getObjectivesCount(this, entityPlayer);
	}

	public boolean isObjectiveCompleted(EntityPlayer entityPlayer, int objectiveID) {
		return quest.isObjectiveCompleted(this, entityPlayer, objectiveID);
	}

	public Entity getGiver() {
		return giver;
	}

	public void setGiver(Entity entity) {
		this.giver = entity;
		this.giverUniqueID = giver.getUniqueID();
	}

	public boolean isGiver(Entity entity) {
		if (giver != null && giver == entity) {
			return true;
		}
		return giverUniqueID != null && entity.getUniqueID().equals(giverUniqueID);
	}

	public boolean hasGiver() {
		if (getGiver() != null) {
			return true;
		}
		return giverUniqueID != null;
	}

	public void addRewards(List<IQuestReward> rewards, EntityPlayer entityPlayer) {
		quest.addToRewards(this, entityPlayer, rewards);
	}

	public IQuest getQuest() {
		return quest;
	}

	public NBTTagCompound getTagCompound() {
		return tagCompound;
	}

	public void setTagCompound(NBTTagCompound tagCompound) {
		this.tagCompound = tagCompound;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void markComplited(EntityPlayer entityPlayer, boolean force) {
		if (force) {
			this.completed = true;
		} else {
			this.quest.setCompleted(this, entityPlayer);
		}
	}

	public QuestStack copy() {
		QuestStack questStack = new QuestStack(this.quest);
		questStack.giverUniqueID = giverUniqueID;
		questStack.giver = giver;
		if (getTagCompound() != null) {
			questStack.setTagCompound((NBTTagCompound) getTagCompound().copy());
		}
		return questStack;
	}

	public ItemStack getContract() {
		ItemStack contract = new ItemStack(MatterOverdrive.ITEMS.contract);
		NBTTagCompound questTag = new NBTTagCompound();
		writeToNBT(questTag);
		contract.setTagCompound(questTag);
		return contract;
	}

	public boolean canAccept(EntityPlayer entityPlayer, QuestStack questStack) {
		return quest.canBeAccepted(questStack, entityPlayer);
	}
}
