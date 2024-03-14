package matteroverdrive.items.weapon;

import matteroverdrive.api.inventory.IEnergyPack;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyPack extends MOBaseItem implements IEnergyPack {

	public static int ENERGYPACKVALUE = 32000;
	public EnergyPack(String name) {
		super(name);
	}

	public boolean hasDetails(ItemStack stack) {
		return true;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 0.0;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return 15866137;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
		super.addDetails(itemstack, player, worldIn, infos);
		infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(null, getEnergyAmount(itemstack)));
	}

	@Override
	public int getEnergyAmount(ItemStack pack) {
		return ENERGYPACKVALUE;
	}
}
