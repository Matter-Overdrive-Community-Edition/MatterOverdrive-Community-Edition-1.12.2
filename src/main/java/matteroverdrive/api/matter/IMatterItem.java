
package matteroverdrive.api.matter;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/16/2015. This is used by items that have specific
 * amount of matter based on their items stack. This is a dynamic way of getting
 * matter based on each Item Stack. For example the item matter can be based in
 * it's damage or NBT tag.
 */
public interface IMatterItem {
	/**
	 * @param itemStack the item stack.
	 * @return the matter of the Item stack.
	 */
	int getMatter(ItemStack itemStack);
}
