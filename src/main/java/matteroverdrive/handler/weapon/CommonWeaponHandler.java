
package matteroverdrive.handler.weapon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.weapon.MOEventEnergyWeapon;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class CommonWeaponHandler {
	private static final PacketFirePlasmaShot.BiHandler firePlasmaShotHandler = new PacketFirePlasmaShot.BiHandler();
	private final Map<EntityPlayer, Long> weaponTimestamps;

	public CommonWeaponHandler() {
		weaponTimestamps = new HashMap<>();
	}

	public void addTimestamp(EntityPlayer player, long timestamp) {
		weaponTimestamps.put(player, timestamp);
	}

	public boolean hasTimestamp(EntityPlayer player) {
		return weaponTimestamps.containsKey(player);
	}

	public long getTimestamp(EntityPlayer entityPlayer) {
		if (entityPlayer != null) {
			return weaponTimestamps.get(entityPlayer);
		}
		return 0;
	}

	public void handlePlasmaShotFire(EntityPlayer entityPlayer, PacketFirePlasmaShot plasmaShot, long timeStamp) {
		int delay = (int) (timeStamp - getTimestamp(entityPlayer));
		firePlasmaShotHandler.handleServerShot(entityPlayer, plasmaShot, delay);
		MatterOverdrive.NETWORK.sendToAllAround(plasmaShot, entityPlayer, plasmaShot.getShot().getRange() + 64);
	}

	@SubscribeEvent
	public void onEnergyWeaponEvent(MOEventEnergyWeapon eventEnergyWeapon) {
		if (eventEnergyWeapon.getEntity() != null) {
			AndroidPlayer androidPlayer = MOPlayerCapabilityProvider
					.GetAndroidCapability(eventEnergyWeapon.getEntity());
			if (androidPlayer != null) {
				androidPlayer.onWeaponEvent(eventEnergyWeapon);
			}
		}
	}
}
