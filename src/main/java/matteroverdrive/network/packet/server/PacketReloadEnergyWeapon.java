
package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloadEnergyWeapon extends PacketAbstract {
	public PacketReloadEnergyWeapon() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class ServerHandler extends AbstractServerPacketHandler<PacketReloadEnergyWeapon> {
		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketReloadEnergyWeapon message, MessageContext ctx) {
			ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (!heldItem.isEmpty()
					&& heldItem.getItem() instanceof EnergyWeapon) {
				if (((EnergyWeapon) heldItem.getItem())
						.needsRecharge(heldItem)) {
					((EnergyWeapon) heldItem.getItem())
							.chargeFromEnergyPack(heldItem, player);
				}
			}
		}
	}
}