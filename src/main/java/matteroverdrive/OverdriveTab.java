
package matteroverdrive;

import matteroverdrive.util.MOLog;
import matteroverdrive.util.StackUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.concurrent.Callable;

public class OverdriveTab extends CreativeTabs {
	private ItemStack itemstack = ItemStack.EMPTY;
	private final Callable<ItemStack> stackCallable;

	public OverdriveTab(String label, Callable<ItemStack> stackCallable) {
		super(label);
		this.stackCallable = stackCallable;
	}

	@Override
	public ItemStack createIcon() {
		return null;
	}

	@Override
	public ItemStack getIcon() {
		if (StackUtils.isNullOrEmpty(itemstack)) {
			if (stackCallable != null) {
				try {
					itemstack = stackCallable.call();
				} catch (Exception e) {
					MOLog.error(e.getMessage(), e);
				}
			} else {
				itemstack = new ItemStack(MatterOverdrive.ITEMS.matter_scanner);
			}
		}
		return itemstack;
	}
}