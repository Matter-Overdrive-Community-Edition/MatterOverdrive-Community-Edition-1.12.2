
package matteroverdrive.items.android;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public abstract class BionicPart extends MOBaseItem implements IBionicPart {
	public BionicPart(String name) {
		super(name);
		this.setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
	}

	@SideOnly(Side.CLIENT)
	public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
		super.addDetails(itemstack, player, worldIn, infos);
		Multimap<String, AttributeModifier> multimap = getModifiers(
				MOPlayerCapabilityProvider.GetAndroidCapability(player), itemstack);
		if (multimap != null) {
			multimap.values().forEach(modifier -> {
				switch (modifier.getOperation()) {
				case 0:
					infos.add(
							TextFormatting.GREEN + String.format("%s: +%s", modifier.getName(), modifier.getAmount()));
					break;
				case 1:
					infos.add(TextFormatting.GREEN
							+ String.format("%s: %s", modifier.getName(), (modifier.getAmount() >= 0 ? "+" : "")
									+ DecimalFormat.getPercentInstance().format(modifier.getAmount())));
					break;
				default:
					infos.add(TextFormatting.GREEN + String.format("%s: %s", modifier.getName(),
							DecimalFormat.getPercentInstance().format(modifier.getAmount() + 1)));
				}
			});
		}
	}

	public Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack) {
		Multimap multimap = HashMultimap.create();
		loadCustomAttributes(itemStack, multimap);
		return multimap;
	}

	private void loadCustomAttributes(ItemStack itemStack, Multimap<String, AttributeModifier> multimap) {
		if (itemStack.getTagCompound() != null) {
			NBTTagList attributeList = itemStack.getTagCompound().getTagList("CustomAttributes",
					Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < attributeList.tagCount(); i++) {
				NBTTagCompound tagCompound = attributeList.getCompoundTagAt(i);
				String attributeName = tagCompound.getString("Name");
				double amount = tagCompound.getDouble("Amount");
				int operation = tagCompound.getByte("Operation");
				multimap.put(attributeName, new AttributeModifier(UUID.fromString(tagCompound.getString("UUID")),
						MOStringHelper.translateToLocal("attribute.name." + attributeName), amount, operation));
			}
		}
	}

	public boolean hasDetails(ItemStack stack) {
		return true;
	}
}