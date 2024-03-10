
package matteroverdrive.handler.weapon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.network.packet.bi.PacketWeaponTick;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class ClientWeaponHandler extends CommonWeaponHandler {
	private static final float RECOIL_RESET_SPEED = 0.03f;
	private static final float CAMERA_RECOIL_RESET_SPEED = 0.03f;
	public static float ZOOM_TIME;
	public static float RECOIL_TIME;
	public static float RECOIL_AMOUNT;
	public static float CAMERA_RECOIL_TIME;
	public static float CAMERA_RECOIL_AMOUNT;
	private final Map<IWeapon, Integer> shotTracker;
	private final IntHashMap<PlasmaBolt> plasmaBolts;
	private final Random cameraRecoilRandom = new Random();
	private float lastMouseSensitivity;
	private int nextShotID;
	private boolean hasChangedSensitivity = false;

	public ClientWeaponHandler() {
		shotTracker = new HashMap<>();
		plasmaBolts = new IntHashMap<>();
	}

	public void registerWeapon(IWeapon weapon) {
		shotTracker.put(weapon, 0);
	}

	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().world != null
				&& Minecraft.getMinecraft().player != null) {
			for (IWeapon item : shotTracker.keySet()) {
				int oldTime = shotTracker.get(item);
				if (oldTime > 0) {
					shotTracker.put(item, oldTime - 1);
				}
			}

			manageWeaponView();
		}
	}

	@SideOnly(Side.CLIENT)
	public void onTick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getMinecraft().player != null && event.phase.equals(TickEvent.Phase.END)) {
			EntityPlayer entityPlayer = Minecraft.getMinecraft().player;

            ItemStack heldItem = entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
            if (heldItem.getItem() instanceof IWeapon) {
				if (((IWeapon) heldItem.getItem()).isWeaponZoomed(entityPlayer,
						heldItem)) {
					ZOOM_TIME = Math.min(ZOOM_TIME + (event.renderTickTime * 0.1f), 1);
				} else {
					ZOOM_TIME = Math.max(ZOOM_TIME - (event.renderTickTime * 0.1f), 0);
				}
			} else {
				ZOOM_TIME = Math.max(ZOOM_TIME - (event.renderTickTime * 0.2f), 0);
			}

			if (ZOOM_TIME == 0) {
				if (hasChangedSensitivity) {
					hasChangedSensitivity = false;
					Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity;
				} else {
					lastMouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
				}
			} else if (ZOOM_TIME != 0) {
                if (heldItem.getItem() instanceof IWeapon) {
					hasChangedSensitivity = true;
					Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity
							* (1f - (ZOOM_TIME * ((IWeapon) heldItem.getItem())
									.getZoomMultiply(entityPlayer, heldItem)));
				} else {
					hasChangedSensitivity = true;
					Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity;
				}
			}
			if (RECOIL_TIME > 0) {
				RECOIL_TIME = Math.max(0, RECOIL_TIME - RECOIL_RESET_SPEED);
			}

			if (CAMERA_RECOIL_TIME > 0) {
				CAMERA_RECOIL_TIME = Math.max(0, CAMERA_RECOIL_TIME - CAMERA_RECOIL_RESET_SPEED);
			}
		}
	}

	@SubscribeEvent
	public void onFovUpdate(FOVUpdateEvent event) {
        ItemStack heldItem = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.getItem() instanceof IWeapon) {
			event.setNewfov(event.getNewfov() - event.getFov() * ZOOM_TIME
					* ((IWeapon) heldItem.getItem())
							.getZoomMultiply(Minecraft.getMinecraft().player,
									heldItem));
		}
	}

	private void manageWeaponView() {
		for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
			ItemStack currentitem = player.getHeldItem(EnumHand.MAIN_HAND);
			if (currentitem.getItem() instanceof IWeapon && ((IWeapon) currentitem.getItem()).isAlwaysEquipped(currentitem)) {
				if (player == Minecraft.getMinecraft().player
						&& Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					// this disables the use animation of the weapon in first person
					// to enable custom animations
					currentitem.setItemDamage(0);
				} else {
					// this allows the item to play the bow use animation when in 3rd person mode
					currentitem.setItemDamage(1);
					player.setActiveHand(EnumHand.MAIN_HAND);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void sendWeaponTickToServer(World world, PacketFirePlasmaShot firePlasmaShot) {
		MatterOverdrive.NETWORK.sendToServer(new PacketWeaponTick(world.getWorldTime(), firePlasmaShot));
	}

	public boolean shootDelayPassed(IWeapon item) {
		return shotTracker.get(item) <= 0;
	}

	public void addShootDelay(IWeapon item, ItemStack weaponStack) {
		if (shotTracker.containsKey(item)) {
			shotTracker.put(item, shotTracker.get(item) + item.getShootCooldown(weaponStack));
		}
	}

	public void addReloadDelay(IWeapon weapon, int delay) {
		if (shotTracker.containsKey(weapon)) {
			shotTracker.put(weapon, shotTracker.get(weapon) + delay);
		}
	}

	public void setRecoil(float amount, float time, float viewRecoilMultiply) {
		RECOIL_AMOUNT = amount;
		RECOIL_TIME = time;
		Minecraft.getMinecraft().player.rotationPitch -= amount * viewRecoilMultiply;
	}

	public void setCameraRecoil(float amount, float time) {
		CAMERA_RECOIL_AMOUNT = amount * (cameraRecoilRandom.nextBoolean() ? -1 : 1);
		CAMERA_RECOIL_TIME = time;
	}

	public float getEquippedWeaponAccuracyPercent(EntityPlayer entityPlayer) {
        ItemStack heldItem = entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.getItem() instanceof IWeapon) {
			return ((IWeapon) heldItem.getItem()).getAccuracy(
					heldItem, entityPlayer,
					((IWeapon) heldItem.getItem()).isWeaponZoomed(entityPlayer,
							heldItem))
					/ ((IWeapon) heldItem.getItem())
							.getMaxHeat(heldItem);
		}
		return 0;
	}

	public void addPlasmaBolt(PlasmaBolt plasmaBolt) {
		plasmaBolts.addKey(plasmaBolt.getEntityId(), plasmaBolt);
	}

	public void removePlasmaBolt(PlasmaBolt plasmaBolt) {
		plasmaBolts.removeObject(plasmaBolt.getEntityId());
	}

	public PlasmaBolt getPlasmaBolt(int id) {
		return plasmaBolts.lookup(id);
	}

	public int getNextShotID() {
		return nextShotID++;
	}

	public WeaponShot getNextShot(ItemStack weaponStack, EnergyWeapon energyWeapon, EntityLivingBase shooter,
			boolean zoomed) {
		return new WeaponShot(getNextShotID(), energyWeapon.getWeaponScaledDamage(weaponStack, shooter),
				energyWeapon.getAccuracy(weaponStack, shooter, zoomed), WeaponHelper.getColor(weaponStack),
				energyWeapon.getRange(weaponStack));
	}
}
