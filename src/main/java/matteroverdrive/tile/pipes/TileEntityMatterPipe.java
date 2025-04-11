
package matteroverdrive.tile.pipes;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.transport.IGridNode;
import matteroverdrive.data.MatterStorage;
import matteroverdrive.data.transport.FluidPipeNetwork;
import matteroverdrive.data.transport.IFluidPipe;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.init.OverdriveFluids;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.decomposer.TileEntityMachineDecomposer;
import matteroverdrive.network.packet.client.PacketMatterUpdate;
import matteroverdrive.util.TimeTracker;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class TileEntityMatterPipe extends TileEntityPipe implements IFluidPipe {
    public static Random rand = new Random();
    protected final MatterStorage storage;
    protected FluidPipeNetwork fluidPipeNetwork;
    protected int transferSpeed;
    TimeTracker t;

    public TileEntityMatterPipe() {
        t = new TimeTracker();
        storage = new MatterStorage(32);
        this.transferSpeed = 10;
    }

    @Override
    public void update() {
        super.update();
        needsUpdate = true;
        if (!world.isRemote) {
            manageTransfer();
            manageNetwork();
        }
    }

	public boolean establishConnectionFromSide(IBlockState blockState, EnumFacing side) {

		int connCount = getConnectionsCount();
		if (connCount < 1) {
			if (!MOMathHelper.getBoolean(getConnectionsMask(), side.ordinal())) {
				setConnection(side, true);
				world.markBlockRangeForRenderUpdate(pos, pos);
				return true;
			}
		}
		return false;
	}

    public void manageNetwork() {
        if (fluidPipeNetwork == null) {
            if (!tryConnectToNeighborNetworks(world)) {
                FluidPipeNetwork network = MatterOverdrive.FLUID_NETWORK_HANDLER.getNetwork(this);
                network.addNode(this);
            }
        }
    }

	public void manageTransfer() {
		if (storage.getMatterStored() > 0 && getNetwork() != null) {
			for (IFluidPipe pipe : getNetwork().getNodes()) {
				for (EnumFacing direction : EnumFacing.VALUES) {
					TileEntity handler = pipe.getTile().getWorld()
							.getTileEntity(pipe.getTile().getPos().offset(direction));
					if (handler != null && handler.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
							direction.getOpposite()) && !(handler instanceof TileEntityMachineDecomposer) && !(handler instanceof IFluidPipe)) {
						int amount = storage.extractMatter(handler
								.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite())
								.fill(new FluidStack(OverdriveFluids.matterPlasma, storage.getMatterStored()), true),
								false);
						if (amount != 0) {
							if (handler != null && handler.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER,
									direction.getOpposite())) {
								MatterOverdrive.NETWORK.sendToAllAround(new PacketMatterUpdate(handler), handler, 64);
							}
							if (storage.getMatterStored() <= 0) {
								return;
							}
						}

					}
				}
			}
		}
	}

    @Override
    public boolean canConnectToPipe(TileEntity entity, EnumFacing direction) {
        if (entity != null) {
            if (entity instanceof TileEntityMatterPipe) {
                if (this.getBlockType() != entity.getBlockType()) {
                    return false;
                }
                return true;
            }
            return entity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
        }
        return false;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (!world.isRemote && categories.contains(MachineNBTCategory.DATA) && toDisk) {
            storage.writeToNBT(comp);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            storage.readFromNBT(comp);
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

	@Override
	public void onAdded(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			int connectionCount = 0;
			for (EnumFacing enumFacing : EnumFacing.VALUES) {
				BlockPos neighborPos = pos.offset(enumFacing);
				TileEntity tileEntityNeignbor = world.getTileEntity(neighborPos);
				IBlockState neighborState = world.getBlockState(neighborPos);
				if (tileEntityNeignbor instanceof IFluidPipe) {
					if (connectionCount < 2 && ((IFluidPipe) tileEntityNeignbor)
							.establishConnectionFromSide(neighborState, enumFacing.getOpposite())) {
						this.setConnection(enumFacing, true);
						world.markBlockRangeForRenderUpdate(pos, pos);
						connectionCount++;
					}
				}
			}
		}
	}

    public boolean tryConnectToNeighborNetworks(World world) {
        boolean hasConnected = false;
        for (EnumFacing side : EnumFacing.VALUES) {
            TileEntity neighborEntity = world.getTileEntity(pos.offset(side));
            if (neighborEntity instanceof TileEntityMatterPipe && this.getBlockType() == neighborEntity.getBlockType()) {
                if (((TileEntityMatterPipe) neighborEntity).getNetwork() != null && ((TileEntityMatterPipe) neighborEntity).getNetwork() != this.fluidPipeNetwork) {
                    ((TileEntityMatterPipe) neighborEntity).getNetwork().addNode(this);
                    hasConnected = true;
                }
            }
        }
        return hasConnected;
    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
    }
    
    @Override
    public void onChunkUnload() {
    }

	public void breakConnection(IBlockState blockState, EnumFacing side) {
		setConnection(side, false);
		world.markBlockRangeForRenderUpdate(pos, pos);
	}

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {

    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public TileEntity getTile() {
        return this;
    }

    @Override
    public FluidPipeNetwork getNetwork() {
        return fluidPipeNetwork;
    }

    @Override
    public void setNetwork(FluidPipeNetwork network) {
        this.fluidPipeNetwork = network;
    }

    @Override
    public BlockPos getNodePos() {
        return getPos();
    }

    @Override
    public World getNodeWorld() {
        return getWorld();
    }

    @Override
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction) {
    	return isConnectableSide(direction);
    }

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side) {
        return true;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == MatterOverdriveCapabilities.MATTER_HANDLER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == MatterOverdriveCapabilities.MATTER_HANDLER) {
            return (T) storage;
        }
        return super.getCapability(capability, facing);
    }
}