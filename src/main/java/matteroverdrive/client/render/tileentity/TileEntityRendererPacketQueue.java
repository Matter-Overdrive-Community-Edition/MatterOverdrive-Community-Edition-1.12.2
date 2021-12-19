
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.blocks.BlockNetworkSwitch;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import matteroverdrive.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class TileEntityRendererPacketQueue extends TileEntitySpecialRenderer<TileEntityMachinePacketQueue> {
    final Block fakeBlock = new BlockNetworkSwitch(Material.IRON, "fake_block");

    @Override
    public void render(TileEntityMachinePacketQueue tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.shouldRender())
            return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (tileEntity.flashTime > 0) {
            renderBlock(fakeBlock);
        }

        GlStateManager.popMatrix();
    }

    private void renderBlock(Block block) {
        float distance = 0.1f;

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
        RenderUtils.disableLightmap();
        RenderUtils.drawCube(-0.01, -0.01, -0.01, 1.02, 1.02, 1.02, Reference.COLOR_HOLO);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        RenderUtils.enableLightmap();
    }
}