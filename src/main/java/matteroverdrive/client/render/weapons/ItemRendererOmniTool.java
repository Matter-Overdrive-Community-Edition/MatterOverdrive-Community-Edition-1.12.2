
package matteroverdrive.client.render.weapons;/* Created by Simeon on 10/17/2015. */

import matteroverdrive.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class ItemRendererOmniTool extends WeaponItemRenderer {
    public static final String MODEL = Reference.PATH_MODEL + "item/wielder.obj";

    public ItemRendererOmniTool() {
        super(new ResourceLocation(MODEL));
    }

    @Override
    public void renderHand(RenderPlayer renderPlayer) {
        renderPlayer.renderRightArm(Minecraft.getMinecraft().player);
    }

    @Override
    public void transformHand(float recoilValue, float zoomValue) {
        transformRecoil(recoilValue, zoomValue);
        GlStateManager.translate(0.145, -0.02, -0.15);
        GlStateManager.rotate(3, 0, 1, 0);
        GlStateManager.rotate(105, 1, 0, 0);
        GlStateManager.scale(0.4, 0.4, 0.4);
    }

    @Override
    public float getHorizontalSpeed() {
        return 0.1f;
    }
}
