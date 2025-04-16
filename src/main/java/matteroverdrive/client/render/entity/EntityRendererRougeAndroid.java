
package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class EntityRendererRougeAndroid<T extends EntityRougeAndroidMob> extends RenderBiped<T> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.PATH_ENTITIES + "android.png");
	public static final ResourceLocation TEXTURE_HOLOGRAM = new ResourceLocation(
			Reference.PATH_ENTITIES + "android_holo.png");
	public static final ResourceLocation TEXTURE_COLORLESS = new ResourceLocation(
			Reference.PATH_ENTITIES + "android_colorless.png");
	public static final List<String> NAMES = Arrays.asList("InfiniteBlock", "the_codedone", "chairosto", "buuz135");

	private final boolean hologram;

	public EntityRendererRougeAndroid(RenderManager renderManager, ModelBiped modelBiped, float f, boolean hologram) {
		super(renderManager, modelBiped, f);
		this.hologram = hologram;
		this.addLayer(new LayerBipedArmor(this));
	}

	public EntityRendererRougeAndroid(RenderManager renderManager, boolean hologram) {
		this(renderManager, new ModelBiped(), 0, hologram);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		if (entity.hasCustomName() && NAMES
				.contains(TextFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag().toLowerCase()))) {
			return TEXTURE_COLORLESS;
		} else if (hologram) {
			return TEXTURE_HOLOGRAM;
		} else {
			return TEXTURE;
		}
	}

	@Override
	protected boolean canRenderName(T entity) {
		return entity.getTeam() != null || Minecraft.getMinecraft().player.getDistance(entity) < 18;
	}

	@Override
	protected void preRenderCallback(T entity, float partialTicks) {
		if (entity.getIsLegendary()) {
			GlStateManager.scale(1.5, 1.5, 1.5);
		}
		if (entity.hasCustomName() && NAMES
				.contains(TextFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag().toLowerCase()))) {
			float speed = 360 * 0.2f;
			int hsb = (int) (entity.world.getTotalWorldTime() % speed);
			Color color = Color.getHSBColor(hsb / speed, 0.5f, 1f);
			GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f);
		}
		super.preRenderCallback(entity, partialTicks);
	}

}
