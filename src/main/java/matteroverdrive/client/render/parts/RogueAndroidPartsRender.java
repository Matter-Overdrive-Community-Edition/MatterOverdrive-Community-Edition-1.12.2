package matteroverdrive.client.render.parts;
/*
import javax.annotation.Nonnull;
import matteroverdrive.Reference;
//import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderer;
//import matteroverdrive.client.render.entity.EntityRendererRougeAndroid;
import matteroverdrive.entity.android_player.AndroidPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RogueAndroidPartsRender extends RenderPlayer {

	public boolean doMuscles = false;
	public boolean doRobo = false;
	public boolean doRusty = false;

	public RogueAndroidPartsRender(RenderManager renderManager, boolean arms) {
		super(renderManager, arms);
	}

	private static final ResourceLocation muscles = new ResourceLocation(Reference.PATH_ENTITIES + "android-player.png");
	private static final ResourceLocation robo = new ResourceLocation(Reference.PATH_ENTITIES + "android-player.png");
	private static final ResourceLocation roboRanged = new ResourceLocation(Reference.PATH_ENTITIES + "android_ranged-player.png");

	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
		return doRusty ? roboRanged : doRobo ? robo : doMuscles ? muscles : super.getEntityTexture(entity);
	}

	public void setMainModel(ModelPlayer modelPlayer) {
		mainModel = modelPlayer;
	}

	@Override
	public void renderRightArm(AbstractClientPlayer clientPlayer) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(robo);
		super.renderRightArm(clientPlayer);

		if (Minecraft.getMinecraft().gameSettings.mainHand != EnumHandSide.RIGHT
				|| !clientPlayer.getHeldItemMainhand().isEmpty()) {
			return;
		}

	}

	@Override
	public void renderLeftArm(AbstractClientPlayer clientPlayer) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(robo);
		super.renderLeftArm(clientPlayer);

		if (Minecraft.getMinecraft().gameSettings.mainHand != EnumHandSide.LEFT
				|| !clientPlayer.getHeldItemMainhand().isEmpty()) {
			return;
		}

	}

	public void doRender(@Nonnull AbstractClientPlayer entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.client.event.RenderPlayerEvent.Pre(entity, this, partialTicks, x, y, z)))
			return;
		if (!entity.isUser() || renderManager.renderViewEntity == entity) {
			double d0 = y;

			if (entity.isSneaking()) {
				d0 = y - 0.125D;
			}

			setModelVisibilities(entity);
			GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
			doRenderELB(entity, x, d0, z, entityYaw, partialTicks);
			GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.client.event.RenderPlayerEvent.Post(entity, this, partialTicks, x, y, z));
	}

	public void doRenderELB(AbstractClientPlayer entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		// if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new
		// net.minecraftforge.client.event.RenderLivingEvent.Pre<>(entity, this,
		// partialTicks, x, y, z))) return;
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		mainModel.swingProgress = getSwingProgress(entity, partialTicks);
		boolean shouldSit = entity.isRiding()
				&& (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
		mainModel.isRiding = shouldSit;
		mainModel.isChild = entity.isChild();

		ItemStack head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		ItemStack body = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		ItemStack legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		ItemStack shoes = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		ItemStack heldItem = InventoryPlayer.isHotbar(entity.inventory.currentItem)
				? entity.inventory.mainInventory.get(entity.inventory.currentItem)
				: null;
		ItemStack offHand = entity.inventory.offHandInventory.get(0);

		try {
			float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float netHeadYaw = f1 - f;

			if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
				f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset,
						partialTicks);
				netHeadYaw = f1 - f;
				float f3 = MathHelper.wrapDegrees(netHeadYaw);

				if (f3 < -85.0F) {
					f3 = -85.0F;
				}

				if (f3 >= 85.0F) {
					f3 = 85.0F;
				}

				f = f1 - f3;

				if (f3 * f3 > 2500.0F) {
					f += f3 * 0.2F;
				}

				netHeadYaw = f1 - f;
			}

			float headPitch = entity.prevRotationPitch
					+ (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			renderLivingAt(entity, x, y, z);
			float ageInTicks = handleRotationFloat(entity, partialTicks);
			applyRotations(entity, ageInTicks, f, partialTicks);
			float scaleFactor = prepareScale(entity, partialTicks);
			float limbSwingAmount = 0.0F;
			float limbSwing = 0.0F;

			if (!entity.isRiding()) {
				limbSwingAmount = entity.prevLimbSwingAmount
						+ (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
				limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

				if (entity.isChild()) {
					limbSwing *= 3.0F;
				}

				if (limbSwingAmount > 1.0F) {
					limbSwingAmount = 1.0F;
				}
				netHeadYaw = f1 - f;
			}

			GlStateManager.enableAlpha();
			mainModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
			mainModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor,
					entity);

			if (renderOutlines) {
				boolean wasTeamColorChanged = setScoreTeamColor(entity);
				GlStateManager.enableColorMaterial();
				GlStateManager.enableOutlineMode(getTeamColor(entity));

				if (!renderMarker) {
					renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
				}

				if (!entity.isSpectator()) {
					renderLayers(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
							scaleFactor);
				}

				GlStateManager.disableOutlineMode();
				GlStateManager.disableColorMaterial();

				if (wasTeamColorChanged) {
					unsetScoreTeamColor();
				}
			} else {
				boolean wasBrightnessChanged = setDoRenderBrightness(entity, partialTicks);
				renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
				if (wasBrightnessChanged) {
					unsetBrightness();
				}

				GlStateManager.depthMask(true);

				if (!entity.isSpectator()) {
					renderLayers(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
							scaleFactor);
				}
			}

			GlStateManager.disableRescaleNormal();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		entity.inventory.armorInventory.set(EntityEquipmentSlot.HEAD.getIndex(), head);
		entity.inventory.armorInventory.set(EntityEquipmentSlot.CHEST.getIndex(), body);
		entity.inventory.armorInventory.set(EntityEquipmentSlot.LEGS.getIndex(), legs);
		entity.inventory.armorInventory.set(EntityEquipmentSlot.FEET.getIndex(), shoes);
		if (heldItem != null) {
			entity.inventory.mainInventory.set(entity.inventory.currentItem, heldItem);
		}
		entity.inventory.offHandInventory.set(0, offHand);

//		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		// From Render.class
		if (!renderOutlines) {
			renderName(entity, x, y, z);
		}
		// net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new
		// net.minecraftforge.client.event.RenderLivingEvent.Post<>(entity, this,
		// partialTicks, x, y, z));
	}

	private void setModelVisibilities(@Nonnull AbstractClientPlayer clientPlayer) {
		ModelPlayer modelplayer = getMainModel();

		if (clientPlayer.isSpectator()) {
			modelplayer.setVisible(false);
			modelplayer.bipedHead.showModel = true;
			modelplayer.bipedHeadwear.showModel = true;
		} else {
			ItemStack itemstack = clientPlayer.getHeldItemMainhand();
			ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
			modelplayer.setVisible(true);
			modelplayer.bipedHeadwear.showModel = !modelplayer.bipedHead.isHidden
					&& clientPlayer.isWearing(EnumPlayerModelParts.HAT);
			modelplayer.bipedBodyWear.showModel = !modelplayer.bipedBody.isHidden
					&& clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
			modelplayer.bipedLeftLegwear.showModel = !modelplayer.bipedLeftLeg.isHidden
					&& clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
			modelplayer.bipedRightLegwear.showModel = !modelplayer.bipedRightLeg.isHidden
					&& clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
			modelplayer.bipedLeftArmwear.showModel = !modelplayer.bipedLeftArm.isHidden
					&& clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
			modelplayer.bipedRightArmwear.showModel = !modelplayer.bipedRightArm.isHidden
					&& clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
			modelplayer.isSneak = clientPlayer.isSneaking();
			ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
			ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

			if (!itemstack.isEmpty()) {
				modelbiped$armpose = ModelBiped.ArmPose.ITEM;

				if (clientPlayer.getItemInUseCount() > 0) {
					EnumAction enumaction = itemstack.getItemUseAction();

					if (enumaction == EnumAction.BLOCK) {
						modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
					} else if (enumaction == EnumAction.BOW) {
						modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}

			if (!itemstack1.isEmpty()) {
				modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

				if (clientPlayer.getItemInUseCount() > 0) {
					EnumAction enumaction1 = itemstack1.getItemUseAction();

					if (enumaction1 == EnumAction.BLOCK) {
						modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
					}
				}
			}

			if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
				modelplayer.rightArmPose = modelbiped$armpose;
				modelplayer.leftArmPose = modelbiped$armpose1;
			} else {
				modelplayer.rightArmPose = modelbiped$armpose1;
				modelplayer.leftArmPose = modelbiped$armpose;
			}
		}
	}

}
*/