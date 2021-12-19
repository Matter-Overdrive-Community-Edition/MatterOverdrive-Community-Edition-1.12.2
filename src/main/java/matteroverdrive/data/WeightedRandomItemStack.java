
package matteroverdrive.data;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import javax.annotation.Nonnull;

public class WeightedRandomItemStack extends WeightedRandom.Item {
    private final ItemStack stack;

    public WeightedRandomItemStack(@Nonnull ItemStack stack) {

        this(stack, 100);
    }

    public WeightedRandomItemStack(@Nonnull ItemStack stack, int weight) {

        super(weight);
        this.stack = stack;
    }

    public ItemStack getStack() {

        if (stack.isEmpty()) {
            return null;
        }
        return stack.copy();
    }
}
