
package matteroverdrive.tile;

import matteroverdrive.api.gravity.AnomalySuppressor;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.blocks.BlockGravitationalAnomaly;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.fx.GravitationalStabilizerBeamParticle;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nonnull;
import java.awt.*;

import static matteroverdrive.util.MOBlockHelper.getAboveSide;

public class TileEntityMachineGravitationalStabilizer extends MOTileEntityMachine implements IMOTickable {
    public static Color color1 = new Color(0xFFFFFF);
    public static Color color2 = new Color(0xFF0000);
    public static Color color3 = new Color(0x115A84);
    RayTraceResult hit;

    public TileEntityMachineGravitationalStabilizer() {
        super(4);
    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote) {
            spawnParticles(world);
            hit = seacrhForAnomalies(world);
        }
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    RayTraceResult seacrhForAnomalies(World world) {
        EnumFacing front = world.getBlockState(getPos()).getValue(MOBlock.PROPERTY_DIRECTION).getOpposite();
        for (int i = 1; i < 64; i++) {
            IBlockState blockState = world.getBlockState(getPos().offset(front, i));
            if (blockState.getBlock() instanceof BlockGravitationalAnomaly || blockState.getMaterial().isOpaque()) {
                return new RayTraceResult(new Vec3d(getPos().offset(front, i)).subtract(Math.abs(front.getDirectionVec().getX() * 0.5), Math.abs(front.getDirectionVec().getY() * 0.5), Math.abs(front.getDirectionVec().getZ() * 0.5)), front.getOpposite(), getPos().offset(front, i));
            }
        }
        return null;
    }

    void manageAnomalies(World world) {
        hit = seacrhForAnomalies(world);
        if (hit != null && world.getTileEntity(hit.getBlockPos()) instanceof TileEntityGravitationalAnomaly) {
            ((TileEntityGravitationalAnomaly) world.getTileEntity(hit.getBlockPos())).suppress(new AnomalySuppressor(getPos(), 20, 0.7f));
        }
    }

    public float getPercentage() {
        if (hit != null) {
            TileEntity tile = world.getTileEntity(hit.getBlockPos());
            if (tile instanceof TileEntityGravitationalAnomaly) {
                return Math.max(0, Math.min((float) (((TileEntityGravitationalAnomaly) tile).getEventHorizon() - 0.3f) / 2.3f, 1f));
            }
        }
        return -1;
    }

    @SideOnly(Side.CLIENT)
    void spawnParticles(World world) {
        if (hit != null && world.getTileEntity(hit.getBlockPos()) instanceof TileEntityGravitationalAnomaly) {
            if (random.nextFloat() < 0.3f) {

                float r = (float) getParticleColorR();
                float g = (float) getParticleColorG();
                float b = (float) getParticleColorB();
                EnumFacing up = getAboveSide(world.getBlockState(getPos()).getValue(MOBlock.PROPERTY_DIRECTION)).getOpposite();
                GravitationalStabilizerBeamParticle particle = new GravitationalStabilizerBeamParticle(world, new Vector3f(getPos().getX() + 0.5f, getPos().getY() + 0.5f, getPos().getZ() + 0.5f), new Vector3f(hit.getBlockPos().getX() + 0.5f, hit.getBlockPos().getY() + 0.5f, hit.getBlockPos().getZ() + 0.5f), new Vector3f(up.getXOffset(), up.getYOffset(), up.getZOffset()), 1f, 0.3f, 80);
                particle.setColor(r, g, b, 1);
                ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(particle, RenderParticlesHandler.Blending.Additive);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 4086 * 2;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB bb = Block.FULL_BLOCK_AABB.offset(getPos());
        if (hit != null) {
            return bb.expand(hit.getBlockPos().getX() - getPos().getX(), hit.getBlockPos().getY() - getPos().getY(), hit.getBlockPos().getZ() - getPos().getZ());
        }
        return bb;
    }

    @Override
    public SoundEvent getSound() {
        return MatterOverdriveSounds.forceField;
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public boolean getServerActive() {
        return hit != null;
    }

    @Override
    public float soundVolume() {
        if (getUpgradeMultiply(UpgradeTypes.Muffler) == 2d) {
            return 0.0f;
        }

        return getPercentage() * 0.5f;
    }

    public double getBeamColorR() {
        float percent = getPercentage();
        if (percent == -1)
            return color3.getRed();
        return (color2.getRed() * percent + color1.getRed() * (1 - percent)) / 255;
    }

    public double getBeamColorG() {
        float percent = getPercentage();
        if (percent == -1)
            return color3.getGreen();
        return (color2.getGreen() * percent + color1.getGreen() * (1 - percent)) / 255;
    }

    public double getBeamColorB() {
        float percent = getPercentage();
        if (percent == -1)
            return color3.getBlue();
        return (color2.getBlue() * percent + color1.getBlue() * (1 - percent)) / 255;
    }

    public double getParticleColorR() {
        return getBeamColorR();
    }

    public double getParticleColorG() {
        return getBeamColorG();
    }

    public double getParticleColorB() {
        return getBeamColorB();
    }

    public RayTraceResult getHit() {
        return hit;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public void onServerTick(TickEvent.Phase phase, World world) {
        if (world == null) {
            return;
        }

        if (phase.equals(TickEvent.Phase.START) && getRedstoneActive()) {
            manageAnomalies(world);
        }
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }
}