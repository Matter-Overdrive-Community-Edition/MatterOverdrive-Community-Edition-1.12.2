
package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.items.DataPad;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDataPadCommands extends PacketAbstract {
	public static final int COMMAND_ORDERING = 1;
	NBTTagCompound data;
	int command;
	EnumHand hand;

	public PacketDataPadCommands() {

	}

	public PacketDataPadCommands(EnumHand hand, ItemStack dataPad) {
		this(hand, dataPad, 0);
	}

	public PacketDataPadCommands(EnumHand hand, ItemStack dataPad, int command) {
		data = new NBTTagCompound();
		if (dataPad != null) {
			if (dataPad.hasTagCompound()) {
				if (command == 0) {
					data = dataPad.getTagCompound();
				}
			}
		}
		this.hand = hand;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		command = buf.readInt();
		data = ByteBufUtils.readTag(buf);
		hand = buf.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(command);
		ByteBufUtils.writeTag(buf, data);
		buf.writeBoolean(hand == EnumHand.MAIN_HAND);
	}

	public static class ServerHandler extends AbstractServerPacketHandler<PacketDataPadCommands> {

		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketDataPadCommands message, MessageContext ctx) {
			ItemStack dataPadStack = player.getHeldItem(message.hand);
			if (dataPadStack != null && dataPadStack.getItem() instanceof DataPad) {
				if (message.command == 0) {
					dataPadStack.setTagCompound(message.data);
				}
			}
		}
	}
}
