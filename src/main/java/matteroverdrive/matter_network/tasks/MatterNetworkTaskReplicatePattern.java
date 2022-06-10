
package matteroverdrive.matter_network.tasks;

import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MatterNetworkTaskReplicatePattern extends MatterNetworkTask {
	ItemPattern pattern;
	int amount;

	public MatterNetworkTaskReplicatePattern() {
		super();
		pattern = new ItemPattern();
	}

	public MatterNetworkTaskReplicatePattern(short itemID, short itemMetadata, byte amount) {
		pattern = new ItemPattern(itemID, itemMetadata);
		this.amount = amount;
	}

	public MatterNetworkTaskReplicatePattern(ItemPattern pattern, int amount) {
		this.pattern = pattern;
		this.amount = amount;
	}

	@Override
	protected void init() {
		setUnlocalizedName("replicate_pattern");
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound != null) {
			pattern.readFromNBT(compound.getCompoundTag("Pattern"));
			amount = compound.getShort("amount");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setShort("amount", (short) amount);
		if (compound != null) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			pattern.writeToNBT(tagCompound);
			compound.setTag("Pattern", tagCompound);
		}
	}

	@Override
	public String getName() {
		return String.format("[%s] %s", amount,
				MOStringHelper.translateToLocal(pattern.getItem().getTranslationKey() + ".name"));
	}

	public ItemPattern getPattern() {
		return pattern;
	}

	public boolean isValid(World world) {
		if (!super.isValid(world)) {
			return false;
		}

		return MatterHelper.getMatterAmountFromItem(pattern.toItemStack(false)) > 0;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
