
package matteroverdrive.items.includes;

import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class MOMachineBlockItem extends ItemBlock {
    public MOMachineBlockItem(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> infos, ITooltipFlag flagIn) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (MOStringHelper.hasTranslation(getTranslationKey() + ".details")) {
                infos.add(TextFormatting.GRAY + MOStringHelper.translateToLocal(getTranslationKey() + ".details"));
            }

            if (stack.hasTagCompound()) {
                if (stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy")) {
                    infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(stack.getTagCompound().getInteger("Energy"), stack.getTagCompound().getInteger("MaxEnergy")));
                    if (stack.getTagCompound().hasKey("PowerSend") && stack.getTagCompound().hasKey("PowerReceive")) {
                        infos.add("Send/Receive: " + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("PowerSend")) + "/" + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("PowerReceive")) + MOEnergyHelper.ENERGY_UNIT + "/t");
                    }
                }
                if (stack.getTagCompound().hasKey("Matter") && stack.getTagCompound().hasKey("MaxMatter")) {
                    infos.add(TextFormatting.BLUE + MatterHelper.formatMatter(stack.getTagCompound().getInteger("Matter"), stack.getTagCompound().getInteger("MaxMatter")));

                    if (stack.getTagCompound().hasKey("MatterSend") && stack.getTagCompound().hasKey("MatterReceive")) {
                        infos.add(TextFormatting.DARK_BLUE + "Send/Receive: " + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("MatterSend")) + "/" + MOStringHelper.formatNumber(stack.getTagCompound().getInteger("MatterReceive")) + MatterHelper.MATTER_UNIT + "/t");
                    }
                }

                showItems(stack, Minecraft.getMinecraft().player, infos);
            }
        } else {
            infos.add(MOStringHelper.MORE_INFO);
        }
    }

    public String getItemStackDisplayName(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            return super.getItemStackDisplayName(itemStack) + String.format(TextFormatting.AQUA + " [%s]" + TextFormatting.RESET, MOStringHelper.translateToLocal("item.info.configured"));
        } else {
            return super.getItemStackDisplayName(itemStack);
        }
    }

    @Override
    public int getDamage(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Energy") && stack.getTagCompound().hasKey("MaxEnergy")) {
            return stack.getTagCompound().getInteger("MaxEnergy") - stack.getTagCompound().getInteger("Energy") + 1;
        }
        return 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("MaxEnergy")) {
            return stack.getTagCompound().getInteger("MaxEnergy");
        }
        return 0;
    }

    private void showItems(ItemStack itemStack, EntityPlayer player, List<String> infos) {
        NBTTagList stackTagList = itemStack.getTagCompound().getCompoundTag("Machine").getTagList("Items", Constants.NBT.TAG_COMPOUND);

        if (stackTagList.tagCount() > 0) {
            infos.add("");
            infos.add(TextFormatting.YELLOW + "Inventory:");
            for (int i = 0; i < stackTagList.tagCount(); i++) {
                ItemStack stack = new ItemStack(stackTagList.getCompoundTagAt(i));
                if (stack.getItem() instanceof IUpgrade) {
                    infos.add("   " + TextFormatting.GREEN + stack.getDisplayName());
                } else {
                    infos.add("   " + infos.add(stack.getDisplayName()));
                }
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDamage(stack) > 0;
    }

}