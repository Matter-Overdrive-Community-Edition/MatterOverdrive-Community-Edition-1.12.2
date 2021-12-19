
package matteroverdrive.blocks;

import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.MachineHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class BlockHoloSign extends BlockMonitor<TileEntityHoloSign> implements IDismantleable {

    public BlockHoloSign(Material material, String name) {
        super(material, name);
        setBoundingBox(new AxisAlignedBB(0, 1, 0, 1, 14 / 16d, 1));
        this.setHardness(20f);
        setHasRotation();
    }

    @Override
    public Class<TileEntityHoloSign> getTileEntityClass() {
        return TileEntityHoloSign.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityHoloSign();
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
        boolean flag;
        EnumFacing l = world.getBlockState(pos).getValue(MOBlock.PROPERTY_DIRECTION);
        flag = false;

        IBlockState nState = world.getBlockState(pos.offset(l));
        if (nState.getBlockFaceShape(world, pos.offset(l), l.getOpposite()) == BlockFaceShape.SOLID) {
            flag = true;
        }

        if (flag) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }

        super.neighborChanged(state, world, pos, blockIn, neighbor);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return MachineHelper.canOpenMachine(worldIn, pos, playerIn, true, "alert.no_rights");
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return MachineHelper.canRemoveMachine(world, player, pos, willHarvest) && world.setBlockToAir(pos);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops) {
        IBlockState blockState = world.getBlockState(pos);
        ItemStack blockItem = new ItemStack(getItemDropped(blockState, world.rand, 1));

        boolean flag = blockState.getBlock().removedByPlayer(blockState, world, pos, player, true);
        super.breakBlock(world, pos, blockState);

        if (flag) {
            blockState.getBlock().onPlayerDestroy(world, pos, blockState);
        }

        if (!returnDrops) {
            dropBlockAsItem(world, pos, blockState, 0);
        } else {
            MOInventoryHelper.insertItemStackIntoInventory(player.inventory, blockItem, EnumFacing.DOWN);
        }

        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(blockItem);
        return list;
    }

    @Override
    public boolean canDismantle(EntityPlayer entityPlayer, World world, BlockPos pos) {
        return true;
    }
}
