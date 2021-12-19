
package matteroverdrive.items;

import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkFilter;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class NetworkFlashDrive extends MOBaseItem implements IMatterNetworkFilter {

    public NetworkFlashDrive(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(itemstack, player, worldIn, infos);
        if (itemstack.hasTagCompound()) {
            NBTTagList list = itemstack.getTagCompound().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_LONG);
            for (int i = 0; i < list.tagCount(); i++) {
                BlockPos pos = BlockPos.fromLong(((NBTTagLong) list.get(i)).getLong());
                IBlockState block = player.world.getBlockState(pos);
                infos.add(String.format("[%s] %s", pos.toString(), block.getBlock().getLocalizedName()));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof IMatterNetworkConnection) {
            BlockPos connectionPosition = tileEntity.getPos();
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }

            boolean hasPos = false;
            NBTTagList list = stack.getTagCompound().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_LONG);
            for (int i = 0; i < list.tagCount(); i++) {
                BlockPos p = BlockPos.fromLong(((NBTTagLong) list.get(i)).getLong());
                if (p.equals(connectionPosition)) {
                    hasPos = true;
                    list.removeTag(i);
                    break;
                }
            }

            if (!hasPos) {
                list.appendTag(new NBTTagLong(pos.toLong()));
            }

            stack.getTagCompound().setTag(IMatterNetworkFilter.CONNECTIONS_TAG, list);

            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public NBTTagCompound getFilter(ItemStack stack) {
        return stack.getTagCompound();
    }
}
