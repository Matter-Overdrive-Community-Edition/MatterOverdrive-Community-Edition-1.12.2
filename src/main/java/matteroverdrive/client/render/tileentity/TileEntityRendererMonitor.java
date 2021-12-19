
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public abstract class TileEntityRendererMonitor<T extends MOTileEntityMachine> extends TileEntitySpecialRenderer<T> {
    public static final ResourceLocation screenTextureBack = new ResourceLocation(Reference.PATH_BLOCKS + "pattern_monitor_holo_back.png");
    public static final ResourceLocation screenTextureGlow = new ResourceLocation(Reference.PATH_FX + "holo_monitor_glow.png");

    @Override
    public void render(T tileEntity, double x, double y, double z, float ticks, int destroyStage, float a) {
        if (!tileEntity.shouldRender())
            return;
        GlStateManager.pushMatrix();

        IBlockState blockState = getWorld().getBlockState(tileEntity.getPos());
        EnumFacing rotation = blockState.getValue(MOBlock.PROPERTY_DIRECTION);

        GlStateManager.pushAttrib();
        RenderUtils.beginDrawinngBlockScreen(x, y, z, rotation, Reference.COLOR_HOLO, tileEntity, -0.65, 1f);
        GlStateManager.translate(0, 0, -0.05);
        drawScreen(tileEntity, ticks);
        RenderUtils.endDrawinngBlockScreen();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public abstract void drawScreen(T tileEntity, float ticks);
}
