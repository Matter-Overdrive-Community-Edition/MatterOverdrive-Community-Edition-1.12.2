
package matteroverdrive.tile;

import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.util.MOLog;
import matteroverdrive.items.includes.MOItemEnergyContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author shadowfacts
 */
public class TileEntityBoundingBox extends TileEntity implements IMOTileEntity, ITickable {

    private int tick = 0;
    private BlockPos ownerPos;
    private Block ownerBlock;

    @Override
    public void update() {
        tick++;
        if (tick == 80) { // update every 4 seconds (assuming 20 TPS)
            tick = 0;

            if (world != null) {
                if (!ownerPresent()) {
                    world.setBlockToAir(getPos());
                }
            }

        }
    }

    private boolean ownerPresent() {
        if (ownerPos != null) {
            return world.getBlockState(ownerPos).getBlock() == ownerBlock;
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        ownerPos = BlockPos.fromLong(tag.getLong("owner"));

        String ownerModid = tag.getString("owner_block_modid");
        String ownerName = tag.getString("owner_block_name");
        Block block = Block.REGISTRY.getObject(new ResourceLocation(ownerModid, ownerName));
        if (block == null) {
            MOLog.error("Missing owner block " + ownerModid + ":" + ownerName);
        } else {
            ownerBlock = block;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (ownerPos != null) {
            tag.setLong("owner", ownerPos.toLong());
        }

        if (ownerBlock != null) {
            ResourceLocation id = ownerBlock.getRegistryName();
            tag.setString("owner_block_modid", id.getNamespace());
            tag.setString("owner_block_name", id.getPath());
        }
        return tag;
    }

    public BlockPos getOwnerPos() {
        return ownerPos;
    }

    public void setOwnerPos(BlockPos ownerPos) {
        this.ownerPos = ownerPos;
    }

    public Block getOwnerBlock() {
        return ownerBlock;
    }

    public void setOwnerBlock(Block ownerBlock) {
        this.ownerBlock = ownerBlock;
    }

    public Optional<TileEntity> getOwnerTile() {
        return Optional.ofNullable(world.getTileEntity(getOwnerPos()));
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {

    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }
 
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
         return getOwnerTile().map(tile -> tile.hasCapability(capability, facing)).orElseGet(() -> super.hasCapability(capability, facing));
		} else {
		 return super.hasCapability(capability, facing);
		}
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
         return getOwnerTile().map(tile -> tile.getCapability(capability, facing)).orElseGet(() -> super.getCapability(capability, facing));
		} else {
		 return super.getCapability(capability, facing);
		}
	}
}