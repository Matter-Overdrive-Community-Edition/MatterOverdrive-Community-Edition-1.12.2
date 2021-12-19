
package matteroverdrive.items.food;

import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class RomulanAle extends MOItemFood {

    public RomulanAle(String name) {
        super(name, 4, 0.6f, false);
        setAlwaysEdible();
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);

        if (!(entityLiving instanceof EntityPlayer)) {
            return stack;
        }
        if (!((EntityPlayer) entityLiving).capabilities.isCreativeMode && !worldIn.isRemote) {
            stack.shrink(1);
        }
        if (!MOPlayerCapabilityProvider.GetAndroidCapability(entityLiving).isAndroid()) {
            entityLiving.addPotionEffect(new PotionEffect(Potion.getPotionById(9), 160, 8));
        }

        if (stack.getCount() > 0) {
            ((EntityPlayer) entityLiving).inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            return stack;
        } else {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.DRINK;
    }
}
