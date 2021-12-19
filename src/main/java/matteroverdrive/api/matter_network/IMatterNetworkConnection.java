
package matteroverdrive.api.matter_network;

import matteroverdrive.api.transport.IPipe;
import matteroverdrive.data.transport.MatterNetwork;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 3/11/2015.
 * Used by all Machines that can Connect to a Matter Network.
 * This also handled if Matter Network cables will be connected to the machine.
 */
public interface IMatterNetworkConnection extends IPipe<MatterNetwork> {
    /**
     * The block position of the Connection.
     * This is the MAC address equivalent.
     * Used mainly in Packet filters to filter the machines the packet can reach.
     *
     * @return the block position of the Matter Network connection.
     */
    @Override
    BlockPos getNodePos();

    boolean establishConnectionFromSide(IBlockState blockState, EnumFacing side);

    void breakConnection(IBlockState blockState, EnumFacing side);
}
