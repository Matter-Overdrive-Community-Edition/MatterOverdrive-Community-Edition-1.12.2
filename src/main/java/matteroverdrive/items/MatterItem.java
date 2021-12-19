
package matteroverdrive.items;

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class MatterItem extends MOBaseItem implements IAdvancedModelProvider {
    public static final String[] subItemNames = {"antimatter", "darkmatter", "redmatter"};

    public MatterItem(String name) {
        super(name);
        this.setHasSubtypes(true);
    }

    public static MatterType getType(ItemStack stack) {
        return MatterType.values()[MathHelper.clamp(stack.getMetadata(), 0, subItemNames.length)];
    }

    @Override
    public String[] getSubNames() {
        return subItemNames;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs)) {
            for (int i = 0; i < subItemNames.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp(stack.getMetadata(), 0, subItemNames.length);
        return super.getTranslationKey() + "." + subItemNames[i];
    }

    public enum MatterType {
        ANTIMATTER,
        DARKMATTER,
        REDMATTER;
    }
}