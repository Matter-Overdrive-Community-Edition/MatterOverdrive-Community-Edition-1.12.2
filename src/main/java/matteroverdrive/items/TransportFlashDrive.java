
package matteroverdrive.items;

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TransportFlashDrive extends MOBaseItem {
    public TransportFlashDrive(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(itemstack, player, worldIn, infos);

        if (hasTarget(itemstack)) {
            BlockPos target = getTarget(itemstack);
            IBlockState state = player.world.getBlockState(target);
            Block block = state.getBlock();
            infos.add(TextFormatting.YELLOW + String.format("[%s] %s", target.toString(), state.getMaterial() != Material.AIR ? block.getLocalizedName() : "Unknown"));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getMaterial() != Material.AIR) {
            if (!worldIn.isRemote) {
                player.sendMessage(
                    new TextComponentString(
                        String.format("Destination set to: %d, %d, %d", pos.getX(), pos.getY(), pos.getZ())
                    )
                );
            }
            setTarget(stack, pos);
            return EnumActionResult.SUCCESS;
        }
        removeTarget(stack);
        return EnumActionResult.FAIL;
    }

    public void setTarget(ItemStack itemStack, BlockPos pos) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setLong("target", pos.toLong());
    }

    public void removeTarget(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            itemStack.setTagCompound(null);
        }
    }

    public BlockPos getTarget(ItemStack itemStack) {
        if (itemStack.getTagCompound() != null) {
            return BlockPos.fromLong(itemStack.getTagCompound().getLong("target"));
        }
        return null;
    }

    public boolean hasTarget(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("target", Constants.NBT.TAG_LONG);
    }
}
