
package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncTransportProgress extends TileEntityUpdatePacket {
    int progress;

    public PacketSyncTransportProgress() {
    }

    public PacketSyncTransportProgress(TileEntityMachineTransporter transporter) {
        super(transporter);
        this.progress = transporter.getTransportTime();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        progress = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(progress);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncTransportProgress> {
        public ClientHandler() {
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSyncTransportProgress message, MessageContext ctx) {
            TileEntity entity = message.getTileEntity(player.world);
            if (entity instanceof TileEntityMachineTransporter) {
                ((TileEntityMachineTransporter) entity).setTransportTime(message.progress);
            }
        }
    }
}
