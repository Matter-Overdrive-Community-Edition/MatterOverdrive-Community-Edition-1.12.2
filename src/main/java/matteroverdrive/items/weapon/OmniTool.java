
package matteroverdrive.items.weapon;/* Created by Simeon on 10/17/2015. */

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.api.weapon.WeaponStats;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.client.sound.WeaponSound;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.fx.PhaserBoltRecoil;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.network.packet.server.PacketDigBlock;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.StackUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import javax.annotation.Nonnull;
import java.util.List;

public class OmniTool extends EnergyWeapon {
    public static final int RANGE = 24;
    private static final int MAX_USE_TIME = 240;
    private static final int ENERGY_PER_SHOT = 512;
    private static final float DIG_POWER_MULTIPLY = 0.03f;
    private static float BLOCK_DAMAGE, STEP_SOUND_COUNTER, LAST_BRAKE_TIME;
    private static BlockPos CURRENT_BLOCK;
    private static EnumFacing LAST_SIDE;

    public OmniTool(String name) {
        super(name, RANGE);
        setHarvestLevel("pickaxe", 3);
        setHarvestLevel("axe", 3);
        setHarvestLevel("shovel", 3);
        this.bFull3D = true;
        this.leftClickFire = true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState i) {
        return true;
    }

    @Override
    protected int getCapacity() {
        return 32000;
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
        return MAX_USE_TIME;
    }

    public int getItemEnchantability() {
        return 1;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (hand == EnumHand.OFF_HAND) {
            return ActionResult.newResult(EnumActionResult.PASS, stack);
        }
        this.TagCompountCheck(stack);

        if (canDig(stack, world)) {
            player.setActiveHand(hand);
            if (world.isRemote) {
                stopMiningLastBlock(player, world);
            }
        }
        if (needsRecharge(stack)) {
            if (world.isRemote)
                chargeFromEnergyPack(stack, player);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (player.world.isRemote && player.equals(Minecraft.getMinecraft().player)) {
            if (canDig(stack, player.world)) {
                RayTraceResult hit = player.rayTrace(getRange(stack), 1);

                if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK) {
                    IBlockState state = player.world.getBlockState(hit.getBlockPos());
					final float hardness = state.getPlayerRelativeBlockHardness((EntityPlayer) player, player.world, hit.getBlockPos());
                    boolean canMine = state.getBlock().canHarvestBlock(player.world, hit.getBlockPos(), (EntityPlayer) player);
                    if (!state.getBlock().isAir(state, player.world, hit.getBlockPos()) && canMine) {
                        ++STEP_SOUND_COUNTER;
                        LAST_SIDE = hit.sideHit;
                        if (isSameBlock(hit.getBlockPos())) {
                            if (BLOCK_DAMAGE >= 1.0F) {
                                MatterOverdrive.NETWORK.sendToServer(new PacketDigBlock(hit.getBlockPos(), PacketDigBlock.Type.HARVEST, hit.sideHit));
                                Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(hit.getBlockPos());
                                BLOCK_DAMAGE = 0.0F;
                                STEP_SOUND_COUNTER = 0.0F;
                            } else if (BLOCK_DAMAGE == 0) {
                                MatterOverdrive.NETWORK.sendToServer(new PacketDigBlock(hit.getBlockPos(), PacketDigBlock.Type.CLICK, hit.sideHit));
                            }
                            BLOCK_DAMAGE = MathHelper.clamp(modifyStatFromModules(WeaponStats.BLOCK_DAMAGE, stack, (BLOCK_DAMAGE + hardness)*2), 0, 1);
                            player.world.sendBlockBreakProgress(player.getEntityId(), hit.getBlockPos(), (int) (BLOCK_DAMAGE * 10));
                        } else {
                            stopMiningLastBlock((EntityPlayer) player, player.world);
                            setLastBlock(hit.getBlockPos());
                        }
                    }
                }

            } else {
                player.stopActiveHand();
            }
        } else {
            DrainEnergy(stack, DIG_POWER_MULTIPLY, false);
        }
    }

    @SideOnly(Side.CLIENT)
    private boolean isSameBlock(BlockPos pos) {
        return CURRENT_BLOCK != null && CURRENT_BLOCK.equals(pos);
    }

    @SideOnly(Side.CLIENT)
    private void setLastBlock(BlockPos pos) {
        CURRENT_BLOCK = pos;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        super.onPlayerStoppedUsing(stack, world, entity, timeLeft);
        if (world.isRemote && entity instanceof EntityPlayer) {
            stopMiningLastBlock((EntityPlayer) entity, world);
        } else {
            int ticks = getMaxItemUseDuration(stack) - timeLeft;
            DrainEnergy(stack, ticks, false);
        }
    }

    @SideOnly(Side.CLIENT)
    private void stopMiningLastBlock(EntityPlayer player, World world) {
        if (CURRENT_BLOCK != null) {
            BLOCK_DAMAGE = 0;
            STEP_SOUND_COUNTER = 0.0F;
            MatterOverdrive.NETWORK.sendToServer(new PacketDigBlock(CURRENT_BLOCK, PacketDigBlock.Type.CANCEL, LAST_SIDE));
            world.sendBlockBreakProgress(player.getEntityId(), CURRENT_BLOCK, -1);
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack p_onBlockDestroyed_1_, World p_onBlockDestroyed_2_, IBlockState p_onBlockDestroyed_3_, BlockPos p_onBlockDestroyed_4_, EntityLivingBase p_onBlockDestroyed_5_) {
        return super.onBlockDestroyed(p_onBlockDestroyed_1_, p_onBlockDestroyed_2_, p_onBlockDestroyed_3_, p_onBlockDestroyed_4_, p_onBlockDestroyed_5_);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.FAIL;
    }

    @Override
    public PlasmaBolt getDefaultProjectile(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot) {
        PlasmaBolt bolt = super.getDefaultProjectile(weapon, shooter, position, dir, shot);
        bolt.setKnockBack(0.05f);
        return bolt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientShot(ItemStack weapon, EntityLivingBase shooter, Vec3d position, Vec3d dir, WeaponShot shot) {
        //ClientProxy.weaponHandler.addShootDelay(this);
        MOPositionedSound sound = new MOPositionedSound(MatterOverdriveSounds.weaponsLaserFire, SoundCategory.PLAYERS, 0.5f + itemRand.nextFloat() * 0.2f, 1.2f + itemRand.nextFloat() * 0.2f);
        sound.setPosition((float) position.x, (float) position.y, (float) position.z);
        Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        spawnProjectile(weapon, shooter, position, dir, shot);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onProjectileHit(RayTraceResult hit, ItemStack weapon, World world, float amount) {
        if (hit.typeOfHit == RayTraceResult.Type.BLOCK && amount == 1) {

            if (itemRand.nextFloat() < 0.8f) {
                Minecraft.getMinecraft().effectRenderer.addEffect(new PhaserBoltRecoil(world, hit.hitVec.x, hit.hitVec.y, hit.hitVec.z, new Color(255, 255, 255)));
            }
        }
    }

    @Override
    public float getWeaponBaseAccuracy(ItemStack weapon, boolean zoomed) {
        return 0.3f + getHeat(weapon) / getMaxHeat(weapon) * 5;
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
        return 80;
    }

    @Override
    public float getWeaponBaseDamage(ItemStack weapon) {
        return 7;
    }

    @Override
    public boolean canFire(ItemStack itemStack, World world, EntityLivingBase shooter) {
        return !isOverheated(itemStack) && DrainEnergy(itemStack, getShootCooldown(itemStack), true) && !isEntitySpectator(shooter);
    }

    @Override
    public float getShotSpeed(ItemStack weapon, EntityLivingBase shooter) {
        return 3;
    }

    public boolean canDig(ItemStack itemStack, World world) {
        return !isOverheated(itemStack) && DrainEnergy(itemStack, DIG_POWER_MULTIPLY, true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vector2f getSlotPosition(int slot, ItemStack weapon) {
        switch (slot) {
            case Reference.MODULE_BATTERY:
                return new Vector2f(170, 115);
            case Reference.MODULE_COLOR:
                return new Vector2f(80, 40);
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
                return new Vector2f(173, 90);
            case Reference.MODULE_COLOR:
                return new Vector2f(125, 72);
            case Reference.MODULE_BARREL:
                return new Vector2f(85, 105);
        }
        return getSlotPosition(slot, weapon);
    }

    @Override
    public boolean supportsModule(int slot, ItemStack weapon) {
        return slot != Reference.MODULE_SIGHTS;
    }

    @Override
    public boolean supportsModule(ItemStack weapon, ItemStack module) {
        return !StackUtils.isNullOrEmpty(module) && !StackUtils.isNullOrEmpty(weapon) && (module.getItem() == MatterOverdrive.ITEMS.weapon_module_color || (module.getItem() == MatterOverdrive.ITEMS.weapon_module_barrel && module.getItemDamage() == WeaponModuleBarrel.BLOCK_BARREL_ID));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onShooterClientUpdate(ItemStack itemStack, World world, EntityPlayer entityPlayer, boolean sendServerTick) {
        if (Mouse.isButtonDown(0) && hasShootDelayPassed()) {
            if (canFire(itemStack, world, entityPlayer)) {
                if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
                itemStack.getTagCompound().setLong("LastShot", world.getTotalWorldTime());
                Vec3d dir = entityPlayer.getLook(1);
                Vec3d pos = getFirePosition(entityPlayer, dir, Mouse.isButtonDown(1));
                WeaponShot shot = createClientShot(itemStack, entityPlayer, Mouse.isButtonDown(1));
                onClientShot(itemStack, entityPlayer, pos, dir, shot);
                addShootDelay(itemStack);
                sendShootTickToServer(world, shot, dir, pos);
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                    ClientProxy.instance().getClientWeaponHandler().setRecoil(6 + getAccuracy(itemStack, entityPlayer, isWeaponZoomed(entityPlayer, itemStack)) * 2, 1, 0.05f);
                    ClientProxy.instance().getClientWeaponHandler().setCameraRecoil(1 + getAccuracy(itemStack, entityPlayer, true) * 0.08f, 1);
                }
                return;
            } else if (needsRecharge(itemStack)) {
                chargeFromEnergyPack(itemStack, entityPlayer);
            }
        }

        super.onShooterClientUpdate(itemStack, world, entityPlayer, sendServerTick);
    }

    @SideOnly(Side.CLIENT)
    private Vec3d getFirePosition(EntityPlayer entityPlayer, Vec3d dir, boolean isAiming) {
        Vec3d pos = entityPlayer.getPositionEyes(1);
        pos = pos.subtract((double) (MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F), 0, (double) (MathHelper.cos(entityPlayer.rotationYaw / 180.0F * (float) Math.PI) * 0.16F));
        pos = pos.add(dir.x, dir.y, dir.z);
        return pos;
    }

    @Override
    public boolean onServerFire(ItemStack weapon, EntityLivingBase shooter, WeaponShot shot, Vec3d position, Vec3d dir, int delay) {
        DrainEnergy(weapon, getShootCooldown(weapon), false);
        float newHeat = (getHeat(weapon) + 4) * 2.7f;
        setHeat(weapon, newHeat);
        manageOverheat(weapon, shooter.world, shooter);
        PlasmaBolt fire = spawnProjectile(weapon, shooter, position, dir, shot);
        fire.simulateDelay(delay);
        weapon.getTagCompound().setLong("LastShot", shooter.world.getTotalWorldTime());
        return true;
    }

    @Override
    public boolean isAlwaysEquipped(ItemStack weapon) {
        return false;
    }

    @Override
    public int getBaseShootCooldown(ItemStack itemStack) {
        return 18;
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
        //return Reference.MOD_ID + ":" +"omni_tool_hum";
        return new WeaponSound(MatterOverdriveSounds.weaponsOmniToolHum, SoundCategory.PLAYERS, (float) entity.posX, (float) entity.posY, (float) entity.posZ, itemRand.nextFloat() * 0.04f + 0.06f, itemRand.nextFloat() * 0.1f + 0.95f);
    }
}