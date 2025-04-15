
package matteroverdrive.data.biostats;

import java.util.ArrayList;
import java.util.List;

import matteroverdrive.api.android.BionicStatGuiInfo;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.client.render.HoloIcons;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractBioticStat implements IBioticStat {
	protected String name;
	boolean showOnHud;
	boolean showOnWheel;
	@SideOnly(Side.CLIENT)
	HoloIcon icon;
	private final int xp;
	private IBioticStat root;
	private BionicStatGuiInfo guiInfo;
	private boolean rootMaxLevel;
	private final List<IBioticStat> competitors;
	private final List<ItemStack> requiredItems;
	private final List<IBioticStat> enabledBlacklist;
	private int maxLevel;

	public AbstractBioticStat(String name, int xp) {
		this.name = name;
		this.xp = xp;
		competitors = new ArrayList<>();
		requiredItems = new ArrayList<>();
		enabledBlacklist = new ArrayList<>();
		maxLevel = 1;
	}

	@Override
	public String getUnlocalizedName() {
		return name;
	}

	protected String getUnlocalizedDetails() {
		return "biotic_stat." + name + ".details";
	}

	@Override
	public String getDisplayName(AndroidPlayer androidPlayer, int level) {
		return MOStringHelper.translateToLocal("biotic_stat." + name + ".name");
	}

	@Override
	public boolean isEnabled(AndroidPlayer android, int level) {
		return checkBlacklistActive(android, level);
	}

	public String getDetails(int level) {
		return MOStringHelper.translateToLocal("biotic_stat." + name + ".details");
	}

	@Override
	public boolean canBeUnlocked(AndroidPlayer android, int level) {
		// if the root is not unlocked then this stat can't be unlocked
		if (root != null && !android.isUnlocked(root, rootMaxLevel ? root.maxLevel() : 1)) {
			return false;
		}
		if (isLocked(android, level)) {
			return false;
		}
		if (!requiredItems.isEmpty() && !android.getPlayer().capabilities.isCreativeMode) {
			for (ItemStack item : requiredItems) {

				if (!hasItem(android, item)) {
					return false;
				}
			}
		}
		return android.isAndroid()
				&& (android.getPlayer().capabilities.isCreativeMode || android.getPlayer().experienceLevel >= xp);
	}

	@Override
	public boolean isLocked(AndroidPlayer androidPlayer, int level) {
		return areCompeditrosUnlocked(androidPlayer);
	}

	protected boolean hasItem(AndroidPlayer player, ItemStack stack) {
		int amountCount = stack.getCount();
		for (int i = 0; i < player.getPlayer().inventory.getSizeInventory(); i++) {
			ItemStack s = player.getPlayer().inventory.getStackInSlot(i);
			if (!s.isEmpty() && s.isItemEqual(stack)) {
				amountCount -= s.getCount();
			}
		}

		return amountCount <= 0;
	}

	@Override
	public void onUnlock(AndroidPlayer android, int level) {
		android.getPlayer().addExperienceLevel(-xp);
		consumeItems(android);
	}

	@Override
	public void onUnlearn(AndroidPlayer androidPlayer, int level) {

	}

	// consume all the necessary items from the player inventory
	// does not check if the items exist
	protected void consumeItems(AndroidPlayer androidPlayer) {
		for (ItemStack itemStack : requiredItems) {
			int itemCount = itemStack.getCount();
			for (int j = 0; j < androidPlayer.getPlayer().inventory.getSizeInventory(); j++) {
				ItemStack pStack = androidPlayer.getPlayer().inventory.getStackInSlot(j);
				if (!pStack.isEmpty() && pStack.isItemEqual(itemStack)) {
					int countShouldTake = Math.min(itemCount, pStack.getCount());
					androidPlayer.getPlayer().inventory.decrStackSize(j, countShouldTake);
					itemCount -= countShouldTake;
				}
			}
		}
	}

	@Override
	public void onTooltip(AndroidPlayer android, int level, List<String> list, int mouseX, int mouseY) {
		String name = TextFormatting.BOLD + getDisplayName(android, level);
		if (maxLevel() > 1) {
			name += TextFormatting.RESET + String.format(" [%s/%s]", level, maxLevel());
		}
		list.add(TextFormatting.WHITE + name);
		String details = getDetails(level + 1);
		String[] detailsSplit = details.split("/n/");
		for (String detail : detailsSplit) {
			list.add(TextFormatting.GRAY + detail);
		}
		if (root != null) {
			String rootLevel = "";
			if (root.maxLevel() > 1) {
				if (rootMaxLevel) {
					rootLevel = " " + root.maxLevel();
				}
			}
			list.add(TextFormatting.DARK_AQUA + MOStringHelper.translateToLocal("gui.tooltip.parent") + ": "
					+ TextFormatting.GOLD + String.format("[%s%s]", root.getDisplayName(android, 0), rootLevel));
		}

		StringBuilder requires = new StringBuilder();
		if (!requiredItems.isEmpty()) {
			for (ItemStack itemStack : requiredItems) {
				if (requires.length() > 0) {
					requires.append(TextFormatting.GRAY + ", ");
				}
				if (itemStack.getCount() > 1) {
					requires.append(TextFormatting.DARK_GREEN).append(itemStack.getCount()).append(" x ");
				}

				requires.append(TextFormatting.DARK_GREEN + "").append(itemStack.getDisplayName()).append("");
			}
		}

		if (requires.length() > 0) {
			list.add(TextFormatting.DARK_AQUA + MOStringHelper.translateToLocal("gui.tooltip.requires") + ": "
					+ requires);
		}

		if (!competitors.isEmpty()) {
			StringBuilder locks = new StringBuilder(TextFormatting.RED + MOStringHelper.translateToLocal("gui.tooltip.locks") + ": ");
			for (IBioticStat compeditor : competitors) {
				locks.append(String.format("[%s] ", compeditor.getDisplayName(android, 0)));
			}
			list.add(locks.toString());
		}

		if (level < maxLevel()) {
			list.add((android.getPlayer().experienceLevel < xp ? TextFormatting.RED : TextFormatting.GREEN) + "XP: "
					+ xp);
		}
	}

	public boolean checkBlacklistActive(AndroidPlayer androidPlayer, int level) {
		for (IBioticStat stat : enabledBlacklist) {
			if (stat.isActive(androidPlayer, level)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void registerIcons(TextureMap textureMap, HoloIcons holoIcons) {
		icon = holoIcons.registerIcon(textureMap, "biotic_stat_" + name, 18);
	}

	public void addReqiredItm(ItemStack stack) {
		requiredItems.add(stack);
	}

	@Override
	public boolean showOnHud(AndroidPlayer android, int level) {
		return showOnHud;
	}

	@Override
	public boolean showOnWheel(AndroidPlayer androidPlayer, int level) {
		return showOnWheel;
	}

	@Override
	public int maxLevel() {
		return maxLevel;
	}

	public IBioticStat getRoot() {
		return root;
	}

	public void setRoot(IBioticStat stat) {
		this.root = stat;
	}

	public void addCompetitor(IBioticStat stat) {
		this.competitors.add(stat);
	}

	public void removeCompetitor(IBioticStat competitor) {
		this.competitors.remove(competitor);
	}

	public List<IBioticStat> getCompetitors() {
		return competitors;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public void setShowOnHud(boolean showOnHud) {
		this.showOnHud = showOnHud;
	}

	public void setShowOnWheel(boolean showOnWheel) {
		this.showOnWheel = showOnWheel;
	}

	public void setGuiInfo(BionicStatGuiInfo guiInfo) {
		this.guiInfo = guiInfo;
	}

	@Override
	public BionicStatGuiInfo getGuiInfo(AndroidPlayer androidPlayer, int level) {
		return guiInfo;
	}

	public List<ItemStack> getRequiredItems() {
		return requiredItems;
	}

	public List<IBioticStat> getEnabledBlacklist() {
		return enabledBlacklist;
	}

	public void addToEnabledBlacklist(IBioticStat stat) {
		enabledBlacklist.add(stat);
	}

	@Override
	public HoloIcon getIcon(int level) {
		return icon;
	}

	@Override
	public int getXP(AndroidPlayer androidPlayer, int level) {
		return xp;
	}

	public boolean areCompeditrosUnlocked(AndroidPlayer androidPlayer) {
		if (!competitors.isEmpty()) {
			for (IBioticStat competitor : competitors) {
				if (androidPlayer.isUnlocked(competitor, 0)) {
					return true;
				}
			}
		}
		return false;
	}
}
