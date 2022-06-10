
package matteroverdrive.tile;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.quest.Quest;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.data.inventory.SlotContract;
import matteroverdrive.data.quest.WeightedRandomQuest;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.WeightedRandom;

import java.util.EnumSet;

public class TileEntityMachineContractMarket extends MOTileEntityMachine {
	public static final int QUEST_GENERATE_DELAY_MIN = 20 * 60 * 30;
	public static final int QUEST_GENERATE_DELAY_PER_SLOT = 20 * 60 * 5;
	public static final int CONTRACT_SLOTS = 18;
	private long lastGenerationTime;

	public TileEntityMachineContractMarket() {
		super(0);
		playerSlotsMain = true;
		playerSlotsHotbar = true;
	}

	@Override
	protected void RegisterSlots(Inventory inventory) {
		super.RegisterSlots(inventory);
		inventory.AddSlot(new RemoveOnlySlot(true));
		for (int i = 0; i < CONTRACT_SLOTS; i++) {
			inventory.AddSlot(new SlotContract(false));
		}
	}

	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			manageContractGeneration();
		}
	}

	protected void manageContractGeneration() {
		if (getRedstoneActive() && getTimeUntilNextQuest() <= 0) {
			generateContract();
		}
	}

	private void generateContract() {
		Quest quest = ((WeightedRandomQuest) WeightedRandom.getRandomItem(random,
				MatterOverdriveQuests.contractGeneration)).getQuest();
		QuestStack questStack = MatterOverdrive.QUEST_FACTORY.generateQuestStack(random, quest);
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getSlot(i).getItem() != null) {
				ItemStack itemStack = inventory.getSlot(i).getItem();
				if (itemStack.getTagCompound() != null) {
					QuestStack qs = QuestStack.loadFromNBT(itemStack.getTagCompound());
					if (questStack.getQuest().areQuestStacksEqual(questStack, qs)) {
						return;
					}
				}
			}
		}

		inventory.addItem(questStack.getContract());
		addGenerationDelay();
		forceSync();
	}

	public void addGenerationDelay() {
		int freeSlots = getFreeSlots();
		lastGenerationTime = world.getTotalWorldTime() + QUEST_GENERATE_DELAY_MIN
				+ (inventory.getSizeInventory() - freeSlots) * QUEST_GENERATE_DELAY_PER_SLOT;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
		super.readCustomNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA)) {
			lastGenerationTime = nbt.getLong("LastGenerationTime");
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
		super.writeCustomNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA)) {
			nbt.setLong("LastGenerationTime", lastGenerationTime);
		}
	}

	public int getFreeSlots() {
		int freeSlots = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getSlot(i).getItem() == null) {
				freeSlots++;
			}
		}
		return freeSlots;
	}

	public int getTimeUntilNextQuest() {
		return Math.max(0, (int) (lastGenerationTime - world.getTotalWorldTime()));
	}

	@Override
	public SoundEvent getSound() {
		return null;
	}

	@Override
	public boolean hasSound() {
		return false;
	}

	@Override
	public boolean getServerActive() {
		return false;
	}

	@Override
	public float soundVolume() {
		return 0;
	}

	@Override
	protected void onMachineEvent(MachineEvent event) {

	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type) {
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}
}
