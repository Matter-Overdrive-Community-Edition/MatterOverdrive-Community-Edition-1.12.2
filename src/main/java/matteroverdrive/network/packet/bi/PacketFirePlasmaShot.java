
package matteroverdrive.network.packet.bi;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketFirePlasmaShot extends PacketAbstract {
	WeaponShot shot;
	private int sender;
	private Vec3d position;
	private Vec3d direction;

	public PacketFirePlasmaShot() {
	}

	public PacketFirePlasmaShot(int sender, Vec3d pos, Vec3d dir, WeaponShot shot) {
		this.shot = shot;
		this.sender = sender;
		this.position = pos;
		this.direction = dir;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.shot = new WeaponShot(buf);
		this.sender = buf.readInt();
		this.position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.direction = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		shot.writeTo(buf);
		buf.writeInt(sender);
		buf.writeDouble(position.x);
		buf.writeDouble(position.y);
		buf.writeDouble(position.z);
		buf.writeFloat((float) direction.x);
		buf.writeFloat((float) direction.y);
		buf.writeFloat((float) direction.z);
	}

	public WeaponShot getShot() {
		return shot;
	}

	public static class BiHandler extends AbstractBiPacketHandler<PacketFirePlasmaShot> {
		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketFirePlasmaShot message, MessageContext ctx) {
			if (player.getEntityId() != message.sender) {
				Entity entity = player.world.getEntityByID(message.sender);
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase livingBase = (EntityLivingBase) entity;
					ItemStack heldItem = livingBase.getHeldItem(EnumHand.MAIN_HAND);
					if (!heldItem.isEmpty()
							&& heldItem.getItem() instanceof EnergyWeapon) {
						((EnergyWeapon) heldItem.getItem()).onClientShot(
								heldItem, livingBase, message.position,
								message.direction, message.shot);
					}
				}

			}
		}

		@Override
		public void handleServerMessage(EntityPlayerMP player, PacketFirePlasmaShot message, MessageContext ctx) {
			handleServerShot(player, message, 0);
			MatterOverdrive.NETWORK.sendToAllAround(message, player, message.shot.getRange() + 64);
		}

		public void handleServerShot(EntityPlayer player, PacketFirePlasmaShot shot, int delay) {
			ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (heldItem.getItem() instanceof EnergyWeapon && ((EnergyWeapon) heldItem.getItem())
                    .canFire(player.getHeldItem(EnumHand.MAIN_HAND), player.world, player)) {
				((EnergyWeapon) heldItem.getItem()).onServerFire(heldItem, player, shot.shot, shot.position,
						shot.direction, delay);
			}
		}
	}
}
