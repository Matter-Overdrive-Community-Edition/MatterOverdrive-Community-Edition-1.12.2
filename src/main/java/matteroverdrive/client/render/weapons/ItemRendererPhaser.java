
package matteroverdrive.client.render.weapons;

import matteroverdrive.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemRendererPhaser extends WeaponItemRenderer {
	public static final String MODEL = Reference.PATH_MODEL + "item/phaser2.obj";
	private static final String TEXTURE_COLOR_MASK = Reference.PATH_ITEM + "phaser_color_mask.png";
	public static ResourceLocation phaserTextureColorMask;

	public ItemRendererPhaser() {
		super(new ResourceLocation(MODEL));
		phaserTextureColorMask = new ResourceLocation(TEXTURE_COLOR_MASK);

		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.mul(getCombinedRotation(45f, 120f, 0f));
		mat.setTranslation(new Vector3f(0.2f, 2.1f, 0f));
		mat.setScale(2.65f);
		transforms.put(ItemCameraTransforms.TransformType.GUI, mat);
	}

	@Override
	public void renderHand(RenderPlayer renderPlayer) {
		renderPlayer.renderRightArm(Minecraft.getMinecraft().player);
	}

	@Override
	public void transformHand(float recoilValue, float zoomValue) {
		transformRecoil(recoilValue, zoomValue);
		GlStateManager.translate(0.157, -0.09, -0.17);
		GlStateManager.rotate(3, 0, 1, 0);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.scale(0.4, 0.4, 0.4);
	}

	@Override
	public float getHorizontalSpeed() {
		return 0.1f;
	}
}
