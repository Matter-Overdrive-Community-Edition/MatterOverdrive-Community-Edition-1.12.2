
package matteroverdrive.api.matter;

import matteroverdrive.data.matter_network.ItemPattern;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/27/2015. Used by items that store Item Patterns such
 * as the {@link matteroverdrive.items.PatternDrive}
 */
public interface IMatterPatternStorage {
	ItemPattern getPatternAt(ItemStack storage, int slot);

	void setItemPatternAt(ItemStack storage, int slot, ItemPattern itemPattern);

	boolean increasePatternProgress(ItemStack itemStack, int slot, int amount);

	/**
	 * Gets the capacity of the storage item. How much pattern can it store.
	 *
	 * @param item The storage stack.
	 * @return the pattern capacity of the storage.
	 */
	int getCapacity(ItemStack item);
}
