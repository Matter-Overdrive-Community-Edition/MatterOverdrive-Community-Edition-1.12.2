package matteroverdrive.items.weapon;

import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.api.weapon.WeaponStats;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.fx.PhaserBoltRecoil;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlasmaShotgun extends EnergyWeapon {
	public static int RANGE = 16;
	public static int MAX_HEAT = 80;
	public static int MAX_CAPACITY = 32000;
	public static int BASE_DAMAGE = 16;
	public static int SHOTSPEED = 3;
	public static int BASE_SHOT_COOLDOWN = 22;
	public static int ENERGY_PER_SHOT = 2560;
	public static int MAX_CHARGE_TIME = 20;

	@SideOnly(Side.CLIENT)
	private MOPositionedSound lastChargingSound;

	public PlasmaShotgun(String name) {
		super(name, RANGE);
		this.setFull3D();
		leftClickFire = true;
	}

	@Override
	protected int getCapacity() {
		return MAX_CAPACITY;
	}

	@Override
	protected int getInput() {
		return 128;
	}

	@Override
	protected int getOutput() {
		return 128;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack item) {
		return MAX_CHARGE_TIME;
	}

	public int getItemEnchantability() {
		return 1;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.NONE;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (hand == EnumHand.OFF_HAND) {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
		this.TagCompountCheck(stack);

		if (canFire(stack, world, player)) {
			player.setActiveHand(hand);
			if (world.isRemote) {
				if (lastChargingSound == null) {
					playChargingSound(player);
				}
			}

		}
		if (needsRecharge(stack)) {
			if (world.isRemote)
				chargeFromEnergyPack(stack, player);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@SideOnly(Side.CLIENT)
	public void playChargingSound(EntityPlayer entityPlayer) {
		lastChargingSound = new MOPositionedSound(MatterOverdriveSounds.weaponsPlasmaShotgunCharging,
				SoundCategory.PLAYERS, 3f + itemRand.nextFloat() * 0.2f, 0.9f * itemRand.nextFloat() * 0.2f);
		lastChargingSound.setPosition((float) entityPlayer.posX, (float) entityPlayer.posY, (float) entityPlayer.posZ);
		Minecraft.getMinecraft().getSoundHandler().playSound(lastChargingSound);
	}

	@SideOnly(Side.CLIENT)
	public void stopChargingSound() {
		Minecraft.getMinecraft().getSoundHandler().stopSound(lastChargingSound);
		lastChargingSound = null;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase entity, int timeLeft) {
	}

	public WeaponShot createShot(ItemStack weapon, EntityLivingBase shooter, boolean zoomed) {
		WeaponShot shot = new WeaponShot(itemRand.nextInt(), getWeaponScaledDamage(weapon, shooter),
				getAccuracy(weapon, shooter, zoomed), WeaponHelper.getColor(weapon), getRange(weapon));
		shot.setCount(getShotCount(weapon, shooter));
		return shot;
	}

	@SideOnly(Side.CLIENT)
	private Vec3d getFirePosition(EntityLivingBase entityPlayer, Vec3d dir, boolean isAiming) {
		Vec3d pos = entityPlayer.getPositionEyes(1);
		pos = pos.add(dir.x, dir.y, dir.z);
		return pos;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
		super.onPlayerStoppedUsing(stack, world, entity, timeLeft);
		if (world.isRemote) {
			if (canFire(stack, entity.world, entity)) {
				int maxCount = getShotCount(stack, entity);
				int timeElapsed = (getMaxItemUseDuration(stack) - timeLeft);
				int count = Math.max(1, (int) ((1f - (timeElapsed / (float) MAX_CHARGE_TIME)) * maxCount));
				float shotPercent = count / (float) getShotCount(stack, entity);
				int ticks = getMaxItemUseDuration(stack) - timeLeft;
				DrainEnergy(stack, ticks, false);
				ClientProxy.instance().getClientWeaponHandler()
						.setCameraRecoil(0.3f + getAccuracy(stack, entity, true) * 0.1f, 1);
				Vec3d dir = entity.getLook(1);
				Vec3d pos = getFirePosition(entity, dir, Mouse.isButtonDown(1));
				WeaponShot shot = createShot(stack, entity, Mouse.isButtonDown(1));
				shot.setCount(count);
				shot.setDamage(15 + ticks);
				shot.setAccuracy(shot.getAccuracy() * shotPercent);
				shot.setRange(shot.getRange() + (int) (shot.getRange() * (1 - shotPercent)));
				onClientShot(stack, entity, pos, dir, shot);
				MatterOverdrive.NETWORK.sendToServer(new PacketFirePlasmaShot(entity.getEntityId(), pos, dir, shot));
				addShootDelay(stack);

				ClientProxy.instance().getClientWeaponHandler().setRecoil(
						15 + (maxCount - count) * 2 + getAccuracy(stack, entity, isWeaponZoomed(entity, stack)) * 2,
						1f + (maxCount - count) * 0.03f, 0.3f);
				stopChargingSound();
				shot.setDamage(15);
			} else {
				entity.stopActiveHand();
			}
		}

	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.FAIL;
	}

	@Override
	public PlasmaBolt getDefaultProjectile(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir,
			WeaponShot shot) {
		PlasmaBolt bolt = super.getDefaultProjectile(weapon, shooter, position, dir, shot);
		bolt.setKnockBack(0.05f);
		return bolt;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot) {
		MOPositionedSound sound = new MOPositionedSound(MatterOverdriveSounds.weaponsPlasmaShotgunShot,
				SoundCategory.PLAYERS, 0.3f + itemRand.nextFloat() * 0.2f, 0.9f + itemRand.nextFloat() * 0.2f);
		sound.setPosition((float) position.x, (float) position.y, (float) position.z);
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		spawnProjectiles(weapon, shooter, position, dir, shot);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onProjectileHit(RayTraceResult hit, ItemStack weapon, World world, float amount) {
		if (hit.typeOfHit == RayTraceResult.Type.BLOCK && amount == 1) {

			if (itemRand.nextFloat() < 0.8f) {
				Minecraft.getMinecraft().effectRenderer.addEffect(new PhaserBoltRecoil(world, hit.hitVec.x,
						hit.hitVec.y, hit.hitVec.z, new Color(255, 255, 255)));
			}
		}
	}

	@Override
	public float getWeaponBaseAccuracy(ItemStack weapon, boolean zoomed) {
		return 5f + getHeat(weapon) / getMaxHeat(weapon) * 0.3f;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos) {

	}

	@Override
	protected int getBaseEnergyUse(ItemStack item) {
		return ENERGY_PER_SHOT / getShootCooldown(item);
	}

	@Override
	protected int getBaseMaxHeat(ItemStack item) {
		return MAX_HEAT;
	}

	@Override
	public float getWeaponBaseDamage(ItemStack weapon) {
		return BASE_DAMAGE;
	}

	@Override
	public boolean canFire(ItemStack itemStack, World world, EntityLivingBase shooter) {
		return !isOverheated(itemStack) && DrainEnergy(itemStack, getShootCooldown(itemStack), true)
				&& !isEntitySpectator(shooter);
	}

	@Override
	public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
		return SHOTSPEED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vector2f getSlotPosition(int slot, ItemStack weapon) {
		switch (slot) {
		case Reference.MODULE_BATTERY:
			return new Vector2f(170, 115);
		case Reference.MODULE_COLOR:
			return new Vector2f(60, 45);
		case Reference.MODULE_BARREL:
			return new Vector2f(60, 115);
		case Reference.MODULE_SIGHTS:
			return new Vector2f(150, 35);
		default:
			return new Vector2f(205, 80 + ((slot - Reference.MODULE_OTHER) * 22));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vector2f getModuleScreenPosition(int slot, ItemStack weapon) {
		switch (slot) {
		case Reference.MODULE_BATTERY:
			return new Vector2f(165, 80);
		case Reference.MODULE_COLOR:
			return new Vector2f(100, 80);
		case Reference.MODULE_BARREL:
			return new Vector2f(90, 90);
		case Reference.MODULE_SIGHTS:
			return new Vector2f(140, 72);
		}
		return getSlotPosition(slot, weapon);
	}

	@Override
	public boolean supportsModule(int slot, ItemStack weapon) {
		return true;

	}

	@Override
	public boolean supportsModule(ItemStack weapon, ItemStack module) {
		return !module.isEmpty() && (module.getItem() == MatterOverdrive.ITEMS.weapon_module_color
				|| (module.getItem() == MatterOverdrive.ITEMS.weapon_module_barrel
						&& module.getItemDamage() != WeaponModuleBarrel.HEAL_BARREL_ID && module.getItemDamage() != WeaponModuleBarrel.BLOCK_BARREL_ID && module.getItemDamage() != WeaponModuleBarrel.EXPLOSION_BARREL_ID));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onShooterClientUpdate(ItemStack itemStack, World world, EntityPlayer entityPlayer,
			boolean sendServerTick) {
		if (Mouse.isButtonDown(0) && hasShootDelayPassed()) {
			if (canFire(itemStack, world, entityPlayer)) {
				if (!itemStack.hasTagCompound())
					itemStack.setTagCompound(new NBTTagCompound());
				itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());
				Vec3d dir = entityPlayer.getLook(1);
				Vec3d pos = getFirePosition(entityPlayer, dir, Mouse.isButtonDown(1));
				WeaponShot shot = createClientShot(itemStack, entityPlayer, Mouse.isButtonDown(1));
				onClientShot(itemStack, entityPlayer, pos, dir, shot);
				addShootDelay(itemStack);
				sendShootTickToServer(world, shot, dir, pos);
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					ClientProxy.instance().getClientWeaponHandler().setRecoil(
							6 + getAccuracy(itemStack, entityPlayer, isWeaponZoomed(entityPlayer, itemStack)) * 2, 1,
							0.05f);
					ClientProxy.instance().getClientWeaponHandler()
							.setCameraRecoil(1 + getAccuracy(itemStack, entityPlayer, true) * 0.08f, 1);
				}
				return;
			} else if (needsRecharge(itemStack)) {
				chargeFromEnergyPack(itemStack, entityPlayer);
			}
		}

		super.onShooterClientUpdate(itemStack, world, entityPlayer, sendServerTick);
	}

	public int getShotCount(ItemStack weapon, EntityLivingBase shooter) {
		return 10;
	}

	public PlasmaBolt[] spawnProjectiles(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir,
			WeaponShot shot) {
		PlasmaBolt[] bolts = new PlasmaBolt[shot.getCount()];
		for (int i = 0; i < shot.getCount(); i++) {
			WeaponShot newShot = new WeaponShot(shot);
			if (shooter.world.isRemote) {
				newShot.setSeed(((ClientWeaponHandler) MatterOverdrive.PROXY.getWeaponHandler()).getNextShotID());
			} else {
				newShot.setSeed(shot.getSeed() + i);
			}
			newShot.setDamage(shot.getDamage() / shot.getCount());
			bolts[i] = new PlasmaBolt(shooter.world, shooter, position, dir, newShot, getShotSpeed(weapon, shooter));
			bolts[i].setWeapon(weapon);
			bolts[i].setRenderSize((getShotCount(weapon, shooter) / shot.getCount()) * 0.5f);

			bolts[i].setFireDamageMultiply(WeaponHelper.modifyStat(WeaponStats.FIRE_DAMAGE, weapon, 0));
			float explosionMultiply = WeaponHelper.modifyStat(WeaponStats.EXPLOSION_DAMAGE, weapon, 0);
			if (explosionMultiply > 0) {
				bolts[i].setExplodeMultiply((getWeaponBaseDamage(weapon) * 0.3f * explosionMultiply) / shot.getCount());
			}
			bolts[i].setKnockBack(0.5f);
			if (WeaponHelper.modifyStat(WeaponStats.RICOCHET, weapon, 0) == 1) {
				bolts[i].markRicochetable();
			}
			shooter.world.spawnEntity(bolts[i]);
		}
		return bolts;
	}

	@SideOnly(Side.CLIENT)
	private Vec3d getFirePosition(EntityPlayer entityPlayer, Vec3d dir, boolean isAiming) {
		Vec3d pos = entityPlayer.getPositionEyes(1);
		pos = pos.subtract((double) (MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F), 0,
				(double) (MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F));
		pos = pos.add(dir.x, dir.y, dir.z);
		return pos;
	}

	@Override
	public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3d position, Vec3d dir,
			int delay) {
		if (shooter instanceof EntityPlayer) {
		if (!((EntityPlayer) shooter).capabilities.isCreativeMode) {
		DrainEnergy(weapon, getShootCooldown(weapon), false);
		float newHeat = (getHeat(weapon) + 4) * 2.7f;
		setHeat(weapon, newHeat);
		manageOverheat(weapon, shooter.world, shooter);
		}
		}
		PlasmaBolt[] fire = spawnProjectiles(weapon, shooter, position, dir, shot);
		for (PlasmaBolt bolt : fire) {
			bolt.simulateDelay(delay);
		}
		weapon.getTagCompound().setLong("LastShot", shooter.world.getTotalWorldTime());
		return true;
	}

	@Override
	public boolean isAlwaysEquipped(ItemStack weapon) {
		return false;
	}

	@Override
	public int getBaseShootCooldown(ItemStack itemStack) {
		return BASE_SHOT_COOLDOWN;
	}

	@Override
	public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isWeaponZoomed(EntityLivingBase entityPlayer, ItemStack weapon) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity) {
		return new WeaponSound(MatterOverdriveSounds.weaponsPlasmaShotgunCharging, SoundCategory.PLAYERS,
				(float) entity.posX, (float) entity.posY, (float) entity.posZ, itemRand.nextFloat() * 0.04f + 0.06f,
				itemRand.nextFloat() * 0.1f + 0.95f);
	}
}