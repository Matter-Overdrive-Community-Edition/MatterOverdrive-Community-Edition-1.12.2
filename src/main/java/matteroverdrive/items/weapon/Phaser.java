
package matteroverdrive.items.weapon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.api.weapon.WeaponStats;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.util.MOPhysicsHelper;
import matteroverdrive.util.WeaponHelper;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Phaser extends EnergyWeapon implements IWeapon {
	public static int RANGE = 18;
	public static int MAX_HEAT = 100;
	public static int MAX_CAPACITY = 32000;
	public static int BASE_DAMAGE = 21;
	public static int BASE_SHOT_COOLDOWN = 10;
	public static int MAX_USE_TIME = 60;
	public static int ENERGY_PER_SHOT = 3072;
	public static final int MAX_LEVEL = 6;
	private static final double ENERGY_MULTIPLY = 2.1;
	private static final int KILL_MODE_LEVEL = 3;
	private static final float KILL_DAMAGE_MULTIPLY = 2f;
	private static final int STUN_SLEEP_MULTIPLY = 5;
	final Map<EntityPlayer, WeaponSound> soundMap;

	public Phaser(String name) {
		super(name, RANGE);
		this.bFull3D = true;
		soundMap = new HashMap<>();
	}

	public static boolean isKillMode(ItemStack item) {
		if (!item.hasTagCompound()) {
			item.setTagCompound(new NBTTagCompound());
		}
		return item.getTagCompound().getByte("power") >= KILL_MODE_LEVEL;
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
	public int getBaseEnergyUse(ItemStack item) {
		this.TagCompountCheck(item);
		byte level = getPowerLevel(item);
		return (int) Math.pow(ENERGY_MULTIPLY, level + 1);
	}

	@Override
	protected int getBaseMaxHeat(ItemStack item) {
		return MAX_HEAT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addCustomDetails(ItemStack weapon, EntityPlayer player, List infos) {
		if (isKillMode(weapon)) {
		infos.add(TextFormatting.RED + "Kill Mode: " + (getPowerLevel(weapon) - 2f));
		} else {
		infos.add(TextFormatting.BLUE + "Stun: " + (GetSleepTime(weapon) / 20f) + "s");
		}
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack item) {
		return MAX_USE_TIME;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.BOW;
	}

	private void ManageShooting(ItemStack item, World w, EntityPlayer player, int useCount) {
		if (w.isRemote) {
			return;
		}

		Vec3d dir = getPlayerLook(player, item);
		RayTraceResult hit = MOPhysicsHelper.rayTrace(player, w, getRange(item), 0,
				new Vec3d(0, player.getEyeHeight(), 0), false, true, dir);
		if (hit != null) {
			Vec3d hitVector = hit.hitVec;

			if (hit.entityHit != null && hit.entityHit instanceof EntityLivingBase) {

				if (hit.entityHit instanceof EntityPlayer && FMLCommonHandler.instance().getSide() == Side.SERVER
						&& !player.getServer().isPVPEnabled()) {
					return;
				}

				DamageSource damageInfo = getDamageSource(item, player);
				float damage = getWeaponScaledDamage(item, player);

				EntityLivingBase el = (EntityLivingBase) hit.entityHit;
				double motionX = el.motionX;
				double motionY = el.motionY;
				double moutionZ = el.motionZ;
				if (damage > 0) {
					el.attackEntityFrom(damageInfo, damage);
					el.motionX = motionX;
					el.motionY = motionY;
					el.motionZ = moutionZ;
				}

				el.addPotionEffect(
						new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), GetSleepTime(item), 100));
				el.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("mining_fatigue"),
						GetSleepTime(item), 100));
				el.addPotionEffect(
						new PotionEffect(Potion.getPotionFromResourceLocation("jump_boost"), GetSleepTime(item), -10));

				if (WeaponHelper.hasStat(WeaponStats.FIRE_DAMAGE, item) && isKillMode(item)) {
					el.setFire(Math.round(modifyStatFromModules(WeaponStats.FIRE_DAMAGE, item, 2)
							* item.getTagCompound().getByte("power")));
				} else if (WeaponHelper.hasStat(WeaponStats.HEAL, item)) {
					el.heal((WeaponHelper.modifyStat(WeaponStats.HEAL, item, 0)
							* item.getTagCompound().getByte("power")));
				}
			} else {
				if (WeaponHelper.hasStat(WeaponStats.FIRE_DAMAGE, item)) {
					BlockPos pos = hit.getBlockPos();

					if (hit.typeOfHit == RayTraceResult.Type.BLOCK
							&& player.world.getBlockState(pos).getBlock().isFlammable(player.world, pos, hit.sideHit)) {
						pos.offset(hit.sideHit);

						if (player.canPlayerEdit(pos, hit.sideHit, item)) {
							if (player.world.isAirBlock(pos)) {
								// player.world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z
								// + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
								player.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
							}
						}
					}
				}
			}

			if (isKillMode(item) && useCount % getShootCooldown(item) == getShootCooldown(item) / 2) {
				if (WeaponHelper.hasStat(WeaponStats.EXPLOSION_DAMAGE, item)) {
					w.createExplosion(player, hitVector.x, hitVector.y, hitVector.z,
							WeaponHelper.modifyStat(WeaponStats.EXPLOSION_DAMAGE, item, 0)
									* item.getTagCompound().getByte("power") - (MAX_LEVEL / 2),
							true);
				}
			}
		}
	}

	public Vec3d getPlayerLook(EntityPlayer player, ItemStack weapon) {
		Vec3d dir = player.getLookVec();
		Vec3d rot = getBeamRotation(weapon, player);
		dir.rotateYaw((float) rot.x);
		dir.rotatePitch((float) rot.y);
		// dir.rotateAroundZ((float)rot.z);
		return dir;
	}

	public Vec3d getBeamRotation(ItemStack weapon, EntityPlayer entityPlayer) {
		double rotationY = (float) Math.toRadians(5)
				* MOEasing.Quart.easeIn(getAccuracy(weapon, entityPlayer, false), 0, 1, 1);
		return new Vec3d(0, rotationY, 0);
	}

	@Override
	public float getWeaponBaseAccuracy(ItemStack weapon, boolean zoomed) {
		return getHeat(weapon) / getMaxHeat(weapon);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (hand == EnumHand.OFF_HAND) {
			return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
		}
		this.TagCompountCheck(itemStackIn);

		if (playerIn.isSneaking()) {
			SwitchModes(worldIn, playerIn, itemStackIn);
			if (worldIn.isRemote) {
			if (isKillMode(itemStackIn)) {
				TextComponentString message = new TextComponentString(
						TextFormatting.GOLD + "Mode: " + TextFormatting.RED + "Kill - " + (getPowerLevel(itemStackIn) - 2f));
						message.setStyle(new Style().setColor(TextFormatting.RED));
						playerIn.sendMessage(message);
				} else {
				TextComponentString message = new TextComponentString(
						TextFormatting.GOLD + "Mode: " + TextFormatting.GREEN + "Stun - " + (GetSleepTime(itemStackIn) / 20f) + "s");
						message.setStyle(new Style().setColor(TextFormatting.RED));
						playerIn.sendMessage(message);
				}
			}
		} else {
			if (canFire(itemStackIn, worldIn, playerIn)) {
				playerIn.setActiveHand(hand);
			}
			if (needsRecharge(itemStackIn)) {
				chargeFromEnergyPack(itemStackIn, playerIn);
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
		}

		return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if (!(player instanceof EntityPlayer)) {
			return;
		}
		if (canFire(stack, player.world, player)) {
			if (player instanceof EntityPlayer) {
			if (!((EntityPlayer) player).capabilities.isCreativeMode) {
			DrainEnergy(stack, 1, false);
			int powerLevelMultiply = (getPowerLevel(stack) + 1) / MAX_LEVEL;
			float newHeat = (getHeat(stack) + 1) * (1.1f + (0.05f * powerLevelMultiply));
			setHeat(stack, newHeat);
			manageOverheat(stack, player.world, player);
			}
			ManageShooting(stack, player.world, (EntityPlayer) player, count);
			}
		} else {
			player.resetActiveHand();
		}
	}

	private void SwitchModes(World world, EntityPlayer player, ItemStack item) {
		this.TagCompountCheck(item);
		SoundHandler.PlaySoundAt(world, MatterOverdriveSounds.weaponsPhaserSwitchMode, SoundCategory.PLAYERS, player);
		byte level = getPowerLevel(item);
		level++;
		if (level >= MAX_LEVEL) {
			level = 0;
		}
		setPowerLevel(item, level);
	}

	@Override
	public float getWeaponBaseDamage(ItemStack item) {
		float damage = 0;
		this.TagCompountCheck(item);
		byte level = getPowerLevel(item);
		if (level >= KILL_MODE_LEVEL) {
			damage = (float) Math.pow(KILL_DAMAGE_MULTIPLY, level - (KILL_MODE_LEVEL - 1));
		}
		return damage;
	}

	@Override
	public boolean canFire(ItemStack itemStack, World world, EntityLivingBase shooter) {
		return !isOverheated(itemStack) && DrainEnergy(itemStack, 1, true) && !isEntitySpectator(shooter);
	}

	@Override
	public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot) {

	}

	private int GetSleepTime(ItemStack item) {
		this.TagCompountCheck(item);
		byte level = getPowerLevel(item);
		if (level < KILL_MODE_LEVEL) {
			return (int) (Math.pow(level + 1, STUN_SLEEP_MULTIPLY) * sleepTimeMultipy(item));
		}
		return 0;
	}

	private double sleepTimeMultipy(ItemStack phaser) {
		return WeaponHelper.modifyStat(WeaponStats.DAMAGE, phaser, 1);
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
		default:
			return new Vector2f(200, 60 + ((slot - Reference.MODULE_OTHER) * 22));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vector2f getModuleScreenPosition(int slot, ItemStack weapon) {
		switch (slot) {
		case Reference.MODULE_BATTERY:
			return new Vector2f(165, 90);
		case Reference.MODULE_COLOR:
			return new Vector2f(100, 80);
		case Reference.MODULE_BARREL:
			return new Vector2f(85, 90);
		}
		return getSlotPosition(slot, weapon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onProjectileHit(RayTraceResult hit, ItemStack weapon, World world, float amount) {
		if (hit.getBlockPos() == null) {
			return;
		}

		IBlockState b = world.getBlockState(hit.getBlockPos());
		if (hit.entityHit != null && hit.entityHit instanceof EntityLivingBase) {
			if (WeaponHelper.hasStat(WeaponStats.HEAL, weapon)) {
				world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0, 0,
						0);
			} else if (WeaponHelper.hasStat(WeaponStats.FIRE_DAMAGE, weapon) && isKillMode(weapon)) {
				world.spawnParticle(EnumParticleTypes.FLAME, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0, 0, 0);
			} else {
				if (isKillMode(weapon)) {
					world.spawnParticle(EnumParticleTypes.REDSTONE, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0, 0, 0);
				} else {
					world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0, 0,
							0);
				}
			}

		} else if (!b.getBlock().isAir(b, world, hit.getBlockPos())) {
			if (WeaponHelper.hasStat(WeaponStats.FIRE_DAMAGE, weapon) && isKillMode(weapon)) {
				world.spawnParticle(EnumParticleTypes.FLAME, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0, 0, 0);
			}

			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, 0, 0, 0);
		}
	}

	@Override
	public boolean supportsModule(int slot, ItemStack weapon) {
		return slot != Reference.MODULE_SIGHTS;
	}

	@Override
	public boolean supportsModule(ItemStack weapon, ItemStack module) {
		return !module.isEmpty() && (module.getItem() == MatterOverdrive.ITEMS.weapon_module_barrel && module.getItemDamage() != WeaponModuleBarrel.BLOCK_BARREL_ID
				|| module.getItem() == MatterOverdrive.ITEMS.weapon_module_color);
	}

	@Override
	public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3d position, Vec3d dir,
			int delay) {
		return false;
	}

	@Override
	public boolean isAlwaysEquipped(ItemStack weapon) {
		return false;
	}

	@Override
	public int getBaseShootCooldown(ItemStack weapon) {
		return BASE_SHOT_COOLDOWN;
	}

	@Override
	public float getBaseZoom(ItemStack weapon, EntityLivingBase shooter) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isWeaponZoomed(EntityLivingBase entityPlayer, ItemStack weapon) {
		return false;
	}

	@Override
	public WeaponSound getFireSound(ItemStack weapon, EntityLivingBase entity) {
		return new WeaponSound(MatterOverdriveSounds.weaponsPhaserBeam, SoundCategory.PLAYERS, (float) entity.posX,
				(float) entity.posY, (float) entity.posZ, itemRand.nextFloat() * 0.05f + 0.2f, 1);
	}

	public byte getPowerLevel(ItemStack weapon) {
		if (weapon.hasTagCompound()) {
			return weapon.getTagCompound().getByte("power");
		}
		return 0;
	}

	public void setPowerLevel(ItemStack weapon, byte level) {
		if (weapon.hasTagCompound()) {
			weapon.getTagCompound().setByte("power", level);
		}
	}
}
