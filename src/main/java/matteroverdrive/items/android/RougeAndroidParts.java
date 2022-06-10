
package matteroverdrive.items.android;

import com.google.common.collect.Multimap;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.client.render.entity.EntityRendererRangedRougeAndroid;
import matteroverdrive.client.render.entity.EntityRendererRougeAndroid;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.items.IAdvancedModelProvider;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RougeAndroidParts extends BionicPart implements IBionicPart, IAdvancedModelProvider {
	public static final String[] names = new String[] { "head", "arms", "legs", "chest" };
	final String[] healtModifiersIDs = new String[] { "1bb8df41-63d1-4f58-92c4-43adea7528b2",
			"73983b14-e605-40be-8567-36a9dec51d4f", "29419afc-63ad-4b74-87e2-38219e867119",
			"e4b38c80-7407-48fd-b837-8f36ae516c4d" };

	public RougeAndroidParts(String name) {
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public String[] getSubNames() {
		return names;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@SideOnly(Side.CLIENT)
	public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
		if (itemstack.getTagCompound() != null) {
			if (itemstack.getTagCompound().getByte("Type") == 1) {
				infos.add(TextFormatting.AQUA
						+ MOStringHelper.translateToLocal("item.matteroverdrive.rogue_android_part.range"));
			} else {
				infos.add(TextFormatting.GOLD
						+ MOStringHelper.translateToLocal("item.matteroverdrive.rogue_android_part.melee"));
			}
		} else {
			infos.add(TextFormatting.GOLD
					+ MOStringHelper.translateToLocal("item.matteroverdrive.rogue_android_part.melee"));
		}
		super.addDetails(itemstack, player, worldIn, infos);
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT) public void registerIcons(IIconRegister iconRegister)
	 * { for (int i = 0;i < names.length;i++) { icons[i] =
	 * iconRegister.registerIcon(Reference.MOD_ID + ":" + "rouge_android_" +
	 * names[i]); } }
	 */

	@Override
	public String getTranslationKey(ItemStack stack) {
		int i = MathHelper.clamp(stack.getItemDamage(), 0, 3);
		return super.getTranslationKey() + "." + names[MathHelper.clamp(i, 0, names.length - 1)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		if (isInCreativeTab(creativeTabs))
			for (byte t = 0; t < 2; t++) {
				for (int i = 0; i < names.length; i++) {
					ItemStack stack = new ItemStack(this, 1, i);
					stack.setTagCompound(new NBTTagCompound());
					stack.getTagCompound().setByte("Type", t);
					list.add(stack);
				}
			}
	}

	/*
	 * @SideOnly(Side.CLIENT) public IIcon getIconFromDamage(int damage) { int j =
	 * MathHelper.clamp(damage, 0, names.length-1); return this.icons[j]; }
	 */

	@Override
	public int getType(ItemStack itemStack) {
		return itemStack.getItemDamage();
	}

	@Override
	public boolean affectAndroid(AndroidPlayer player, ItemStack itemStack) {
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack) {
		Multimap multimap = super.getModifiers(player, itemStack);
		if (multimap.isEmpty()) {
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(),
					new AttributeModifier(
							UUID.fromString(healtModifiersIDs[itemStack.getItemDamage()]), MOStringHelper
									.translateToLocal("attribute.name." + SharedMonsterAttributes.MAX_HEALTH.getName()),
							1f, 0));
		}
		return multimap;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getTexture(AndroidPlayer androidPlayer, ItemStack itemStack) {
		if (itemStack.getTagCompound() != null) {
			if (itemStack.getTagCompound().getByte("Type") == 1) {
				return EntityRendererRangedRougeAndroid.texture;
			}
		}
		return EntityRendererRougeAndroid.TEXTURE;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getModel(AndroidPlayer androidPlayer, ItemStack itemStack) {
		int type = getType(itemStack);
		ModelBiped model = ClientProxy.renderHandler.modelMeleeRogueAndroidParts;
		if (itemStack.getTagCompound() != null) {
			if (itemStack.getTagCompound().getByte("Type") == 1) {
				model = ClientProxy.renderHandler.modelRangedRogueAndroidParts;
			}
		}
		model.bipedHead.showModel = type == 0;
		model.bipedHeadwear.showModel = type == 0;
		model.bipedBody.showModel = type == 3;
		model.bipedRightArm.showModel = type == 1;
		model.bipedLeftArm.showModel = type == 1;
		model.bipedRightLeg.showModel = type == 2;
		model.bipedLeftLeg.showModel = type == 2;
		return model;
	}
}