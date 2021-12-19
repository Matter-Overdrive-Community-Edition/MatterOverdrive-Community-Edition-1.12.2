
package matteroverdrive.guide;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MOGuideEntryItem extends MOGuideEntry {
    public MOGuideEntryItem(Item item) {
        super(item.getTranslationKey());
        setStackIcons(item);
    }

    public MOGuideEntryItem(Item itemIcon, String name) {
        super(name, new ItemStack(itemIcon));
    }

    @Override
    public String getDisplayName() {
        return getStackIcons()[0].getDisplayName();
    }
}
