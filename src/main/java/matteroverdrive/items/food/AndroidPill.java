
package matteroverdrive.items.food;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class AndroidPill extends MOItemFood {
	public static final String[] names = new String[] { "red", "blue", "yellow" };

	public AndroidPill(String name) {
		super(name, 0, 0, false);
		setAlwaysEdible();
		hasSubtypes = true;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, @Nullable World worldIn, List<String> infos, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			String[] infoList = MOStringHelper.translateToLocal(getTranslationKey(itemstack) + ".details").split("/n");
			for (String info : infoList) {
				infos.add(TextFormatting.GRAY + info);
			}
		} else {
			infos.add(MOStringHelper.MORE_INFO);
		}

		if (itemstack.getItemDamage() == 2) {
			AndroidPlayer androidPlayer = MOPlayerCapabilityProvider
					.GetAndroidCapability(Minecraft.getMinecraft().player);
			if (androidPlayer != null && androidPlayer.isAndroid()) {
				infos.add(TextFormatting.GREEN + "XP:" + androidPlayer.getResetXPRequired() + "l");
			} else {
				infos.add(TextFormatting.RED + "Not an Android.");
			}
		}
	}

	@Override
	public String getTranslationKey(ItemStack itemStack) {
		return getTranslationKey() + "_" + names[MathHelper.clamp(itemStack.getItemDamage(), 0, names.length - 1)];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			list.add(new ItemStack(this, 1, 0));
			list.add(new ItemStack(this, 1, 1));
			list.add(new ItemStack(this, 1, 2));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(playerIn);
		if (androidPlayer == null)
			return ActionResult.newResult(EnumActionResult.FAIL, stack);

		if (stack.getItemDamage() >= 1) {
			if (!androidPlayer.isTurning() && androidPlayer.isAndroid()) {
				playerIn.setActiveHand(handIn);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
		} else {
			if (!androidPlayer.isAndroid() && !androidPlayer.isTurning()) {
				playerIn.setActiveHand(handIn);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
		}
		return ActionResult.newResult(EnumActionResult.FAIL, stack);
	}

	public void register() {
		setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, this.getTranslationKey().substring(5)));
		GameData.register_impl(this);
	}

	@Override
	protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			return;
		}

		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
		if (itemStack.getItemDamage() == 0) {
			androidPlayer.startConversion();
		} else if (itemStack.getItemDamage() == 1) {
			androidPlayer.setAndroid(false);
		} else if (itemStack.getItemDamage() == 2) {
			if (!androidPlayer.isTurning() && androidPlayer.isAndroid()) {
				int xpLevels = androidPlayer.resetUnlocked();
				player.addExperienceLevel(xpLevels);
			}
		}
	}
}