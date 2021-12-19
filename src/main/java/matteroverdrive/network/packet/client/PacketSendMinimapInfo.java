
package matteroverdrive.network.packet.client;

import io.netty.buffer.ByteBuf;
import matteroverdrive.data.MinimapEntityInfo;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PacketSendMinimapInfo extends PacketAbstract {
    List<MinimapEntityInfo> entityInfos;

    public PacketSendMinimapInfo() {

    }

    public PacketSendMinimapInfo(List<MinimapEntityInfo> entityInfos) {
        this.entityInfos = entityInfos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityInfos = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            entityInfos.add(new MinimapEntityInfo().readFromBuffer(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityInfos.size());
        for (MinimapEntityInfo entityInfo : entityInfos) {
            entityInfo.writeToBuffer(buf);
        }
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSendMinimapInfo> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSendMinimapInfo message, MessageContext ctx) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                AndroidPlayer.setMinimapEntityInfo(message.entityInfos);
            }
        }
    }
}
