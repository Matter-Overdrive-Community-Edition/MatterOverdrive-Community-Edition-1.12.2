package matteroverdrive.handler;
/*
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.client.render.parts.RogueAndroidPartsRender;

import static com.google.common.base.Preconditions.checkNotNull;

public class HandleSkinClient {

	public static final HandleSkinClient INSTANCE = new HandleSkinClient();

	@SideOnly(Side.CLIENT)
	private static final RogueAndroidPartsRender renderSmallArms = new RogueAndroidPartsRender(
			Minecraft.getMinecraft().getRenderManager(), true);

	@SideOnly(Side.CLIENT)
	public static final RogueAndroidPartsRender renderLargeArms = new RogueAndroidPartsRender(
			Minecraft.getMinecraft().getRenderManager(), false);

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRenderPlayerPostEvent(RenderPlayerEvent.Pre event) {
		EntityPlayer entityPlayer = event.getEntityPlayer();
		boolean hasLeftLeg = true;// cyberwareUserData.hasEssential(EnumSlot.LEG, EnumSide.LEFT);
		boolean hasRightLeg = true;// cyberwareUserData.hasEssential(EnumSlot.LEG, EnumSide.RIGHT);
		boolean hasLeftArm = true;// cyberwareUserData.hasEssential(EnumSlot.ARM, EnumSide.LEFT);
		boolean hasRightArm = true;// cyberwareUserData.hasEssential(EnumSlot.ARM, EnumSide.RIGHT);

		boolean robotLeftArm = true;// cyberwareUserData.isCyberwareInstalled(CyberwareContent.cyberlimbs.getCachedStack(ItemCyberlimb.META_LEFT_CYBER_ARM
									// ));
		boolean robotRightArm = true;// cyberwareUserData.isCyberwareInstalled(CyberwareContent.cyberlimbs.getCachedStack(ItemCyberlimb.META_RIGHT_CYBER_ARM));
		boolean robotLeftLeg = true;// cyberwareUserData.isCyberwareInstalled(CyberwareContent.cyberlimbs.getCachedStack(ItemCyberlimb.META_LEFT_CYBER_LEG
									// ));
		boolean robotRightLeg = true;// cyberwareUserData.isCyberwareInstalled(CyberwareContent.cyberlimbs.getCachedStack(ItemCyberlimb.META_RIGHT_CYBER_LEG));
		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(entityPlayer);
		RenderPlayer renderPlayer = event.getRenderer();
		if (!(renderPlayer instanceof RogueAndroidPartsRender)) {
			boolean useSmallArms = renderPlayer.smallArms;
			RogueAndroidPartsRender renderToUse = useSmallArms ? renderSmallArms : renderLargeArms;
			if (robotRightLeg || robotLeftLeg || robotRightArm || robotLeftArm) {
				event.setCanceled(true);
				renderToUse.doRobo = false;
				renderToUse.doRusty = false;
				renderToUse.doRender((AbstractClientPlayer) entityPlayer, event.getX(), event.getY(), event.getZ(),
						entityPlayer.rotationYaw, event.getPartialRenderTick());
				if (androidPlayer != null && androidPlayer.isAndroid()) {
					ModelPlayer mainModel = renderToUse.getMainModel();
					renderToUse.doRobo = true;
					renderToUse.doRender((AbstractClientPlayer) entityPlayer, event.getX(), event.getY(), event.getZ(),
							entityPlayer.rotationYaw, event.getPartialRenderTick());
					mainModel.bipedBody.isHidden = false;
					mainModel.bipedHead.isHidden = false;
					mainModel.bipedLeftArm.isHidden = false;
					mainModel.bipedRightArm.isHidden = false;
					mainModel.bipedLeftLeg.isHidden = false;
					mainModel.bipedRightLeg.isHidden = false;
				}
			}
		}

	}

	private static boolean missingArm = false;
	private static boolean missingSecondArm = false;
	private static boolean hasRoboLeft = false;
	private static boolean hasRoboRight = false;
	private static EnumHandSide oldHand;

	private static final Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void handleRenderHand(RenderHandEvent event) {

		if (missingArm || missingSecondArm || hasRoboLeft || hasRoboRight) {
			float partialTicks = event.getPartialTicks();
			EntityRenderer entityRenderer = mc.entityRenderer;
			event.setCanceled(true);

			boolean isSleeping = mc.getRenderViewEntity() instanceof EntityLivingBase
					&& ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();

			if (mc.gameSettings.thirdPersonView == 0 && !isSleeping && !mc.gameSettings.hideGUI
					&& !mc.playerController.isSpectator()) {
				entityRenderer.enableLightmap();
				renderItemInFirstPerson(partialTicks);
				entityRenderer.disableLightmap();
			}
		}
	}

	public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
		return first != null ? first : checkNotNull(second);
	}

	private void renderItemInFirstPerson(float partialTicks) {
		ItemRenderer itemRenderer = mc.getItemRenderer();
		AbstractClientPlayer abstractclientplayer = mc.player;
		float swingProgress = abstractclientplayer.getSwingProgress(partialTicks);

		EnumHand enumhand = firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);

		float rotationPitch = abstractclientplayer.prevRotationPitch
				+ (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
		float rotationYaw = abstractclientplayer.prevRotationYaw
				+ (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
		boolean doRenderMainHand = true;
		boolean doRenderOffHand = true;

		if (abstractclientplayer.isHandActive()) {
			ItemStack itemstack = abstractclientplayer.getActiveItemStack();

			if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW) // Forge: Data watcher can desync and cause
																			// this to NPE...
			{
				EnumHand enumhand1 = abstractclientplayer.getActiveHand();
				doRenderMainHand = enumhand1 == EnumHand.MAIN_HAND;
				doRenderOffHand = !doRenderMainHand;
			}
		}

		rotateArroundXAndY(rotationPitch, rotationYaw);
		setLightmap();
		rotateArm(partialTicks);
		GlStateManager.enableRescaleNormal();

		if (doRenderMainHand && !missingSecondArm) {
			float f3 = enumhand == EnumHand.MAIN_HAND ? swingProgress : 0.0F;
			// float f5 = 1.0F - (itemRenderer.prevEquippedProgressMainHand +
			// (itemRenderer.equippedProgressMainHand -
			// itemRenderer.prevEquippedProgressMainHand) * partialTicks);
			// RenderCyberlimbHand.INSTANCE.leftRobot = hasRoboLeft;
			// RenderCyberlimbHand.INSTANCE.rightRobot = hasRoboRight;
			// RenderCyberlimbHand.INSTANCE.renderItemInFirstPerson(abstractclientplayer,
			// partialTicks, rotationPitch, EnumHand.MAIN_HAND, f3,
			// itemRenderer.itemStackMainHand, f5);
		}

		if (doRenderOffHand && !missingArm) {
			float f4 = enumhand == EnumHand.OFF_HAND ? swingProgress : 0.0F;
			// float f6 = 1.0F - (itemRenderer.prevEquippedProgressOffHand +
			// (itemRenderer.equippedProgressOffHand -
			// itemRenderer.prevEquippedProgressOffHand) * partialTicks);
			// RenderCyberlimbHand.INSTANCE.leftRobot = hasRoboLeft;
			// RenderCyberlimbHand.INSTANCE.rightRobot = hasRoboRight;
			// RenderCyberlimbHand.INSTANCE.renderItemInFirstPerson(abstractclientplayer,
			// partialTicks, rotationPitch, EnumHand.OFF_HAND, f4,
			// itemRenderer.itemStackOffHand, f6);
		}

		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
	}

	private void rotateArroundXAndY(float angle, float angleY) {
		GlStateManager.pushMatrix();
		GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	private void setLightmap() {
		EntityPlayer entityPlayer = mc.player;
		int i = mc.world.getCombinedLight(new BlockPos(entityPlayer.posX,
				entityPlayer.posY + (double) entityPlayer.getEyeHeight(), entityPlayer.posZ), 0);
		float f = (float) (i & 65535);
		float f1 = (float) (i >> 16);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
	}

	private void rotateArm(float p_187458_1_) {
		EntityPlayerSP entityPlayerSP = mc.player;
		float f = entityPlayerSP.prevRenderArmPitch
				+ (entityPlayerSP.renderArmPitch - entityPlayerSP.prevRenderArmPitch) * p_187458_1_;
		float f1 = entityPlayerSP.prevRenderArmYaw
				+ (entityPlayerSP.renderArmYaw - entityPlayerSP.prevRenderArmYaw) * p_187458_1_;
		GlStateManager.rotate((entityPlayerSP.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((entityPlayerSP.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
	}

	@SubscribeEvent
	public void handleWorldUnload(WorldEvent.Unload event) {
		if (missingArm) {
			GameSettings settings = Minecraft.getMinecraft().gameSettings;
			missingArm = false;
			settings.mainHand = oldHand;
		}
	}
}
*/