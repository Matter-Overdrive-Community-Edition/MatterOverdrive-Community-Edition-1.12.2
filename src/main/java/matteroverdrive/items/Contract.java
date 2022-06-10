
package matteroverdrive.items;

import java.util.List;

import javax.annotation.Nullable;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuest;
import matteroverdrive.api.quest.Quest;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.WeightedRandomQuest;
import matteroverdrive.gui.GuiQuestPreview;
import matteroverdrive.init.MatterOverdriveQuests;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Contract extends MOBaseItem {
	public Contract(String name) {
		super(name);
		setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_CONTRACTS);
	}

	public QuestStack getQuest(ItemStack itemStack) {
		if (itemStack.getTagCompound() != null) {
			return QuestStack.loadFromNBT(itemStack.getTagCompound());
		}
		return null;
	}

	@Override
	public boolean hasDetails(ItemStack stack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
		QuestStack questStack = QuestStack.loadFromNBT(itemstack.getTagCompound());
		if (questStack != null) {
			for (int i = 0; i < questStack.getObjectivesCount(player); i++) {
				infos.add(MatterOverdrive.QUEST_FACTORY.getFormattedQuestObjective(player, questStack, i));
			}
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			MatterOverdrive.QUESTS.getAllQuestName().forEach(name -> {
				ItemStack stack = new ItemStack(this);
				IQuest quest = MatterOverdrive.QUESTS.getQuestByName(name);
				QuestStack questStack = MatterOverdrive.QUEST_FACTORY.generateQuestStack(itemRand, quest);
				NBTTagCompound questTag = new NBTTagCompound();
				questStack.writeToNBT(questTag);
				stack.setTagCompound(questTag);
				items.add(stack);
			});
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		if (itemStack.getTagCompound() != null) {
			QuestStack questStack = QuestStack.loadFromNBT(itemStack.getTagCompound());
			return questStack.getTitle();
		}
		return super.getItemStackDisplayName(itemStack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if (worldIn.isRemote) {
			openGui(itemStackIn);
			return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
		} else {
			QuestStack questStack = getQuest(itemStackIn);
			if (questStack == null) {
				Quest quest = ((WeightedRandomQuest) WeightedRandom.getRandomItem(itemRand,
						MatterOverdriveQuests.contractGeneration)).getQuest();
				questStack = MatterOverdrive.QUEST_FACTORY.generateQuestStack(itemRand, quest);
				NBTTagCompound questTag = new NBTTagCompound();
				questStack.writeToNBT(questTag);
				itemStackIn.setTagCompound(questTag);
				return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
			}
		}
		return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
	}

	@SideOnly(Side.CLIENT)
	private void openGui(ItemStack stack) {
		QuestStack questStack = getQuest(stack);
		if (questStack != null) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiQuestPreview(getQuest(stack)));
		}
	}
}