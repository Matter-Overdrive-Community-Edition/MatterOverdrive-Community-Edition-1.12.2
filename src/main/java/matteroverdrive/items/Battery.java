
package matteroverdrive.items;

import matteroverdrive.items.includes.MOItemEnergyContainer;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Battery extends MOItemEnergyContainer {
	int capacity;
	int input;
	int output;

	public Battery(String name, int capacity, int input, int output) {
		super(name);
		this.capacity = capacity;
		this.input = input;
		this.output = output;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int getInput() {
		return input;
	}

	@Override
	public int getOutput() {
		return output;
	}

	@Override
	public void addDetails(ItemStack stack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
		super.addDetails(stack, player, worldIn, infos);
		infos.add(TextFormatting.GRAY + MOStringHelper.translateToLocal("gui.tooltip.energy.io") + ": " + getInput()
				+ "/" + getOutput() + MOEnergyHelper.ENERGY_UNIT + "/t");
	}
}