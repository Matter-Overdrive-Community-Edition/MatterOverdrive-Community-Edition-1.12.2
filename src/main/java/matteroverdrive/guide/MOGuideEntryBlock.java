
package matteroverdrive.guide;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class MOGuideEntryBlock extends MOGuideEntry {
    public MOGuideEntryBlock(Block block) {
        super(block.getTranslationKey());
        setStackIcons(block);
    }

    public MOGuideEntryBlock(Block blockIcon, String name) {
        super(name, new ItemStack(blockIcon));
    }

    @Override
    public String getDisplayName() {
        if (getStackIcons() != null && getStackIcons().length > 0 && getStackIcons()[0] != null) {
            return getStackIcons()[0].getDisplayName();
        }
        return "Unknown Block";
    }
}
