
package matteroverdrive.network.packet.bi;

import io.netty.buffer.ByteBuf;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.gui.GuiMatterScanner;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PacketMatterScannerGetDatabase extends TileEntityUpdatePacket {
    List<ItemPattern> list;

    public PacketMatterScannerGetDatabase() {
        super();
    }

    public PacketMatterScannerGetDatabase(BlockPos position) {
        super(position);
    }

    public PacketMatterScannerGetDatabase(List<ItemPattern> list) {
        this.list = list;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        int size = buf.readInt();
        for (int i = 0;i < size;i++)
        {
        list.add(new ItemPattern(size));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        //buf.writeInt(list.size());
        //for (ItemPattern pattern : list)
        //{
        //pattern.writeToBuffer(buf);
        //}
    }

    public static class Handler extends AbstractBiPacketHandler<PacketMatterScannerGetDatabase> {

        public Handler() {
        }

        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketMatterScannerGetDatabase message, MessageContext ctx) {
            TileEntity tileEntity = message.getTileEntity(player.world);
            if (tileEntity instanceof IMatterDatabase) {
                IMatterDatabase database = (IMatterDatabase) tileEntity;
                //MatterOverdrive.NETWORK.sendTo(new PacketMatterScannerGetDatabase(database.getPattern()),player);
            }
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketMatterScannerGetDatabase message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiMatterScanner) {
                GuiMatterScanner guiMatterScanner = (GuiMatterScanner) Minecraft.getMinecraft().currentScreen;
                guiMatterScanner.UpdatePatternList(message.list);
            }
        }
    }
}
