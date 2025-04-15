
package matteroverdrive.gui;

import java.util.ArrayList;
import java.util.List;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.container.ContainerAndroidStation;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.inventory.BionicSlot;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.gui.element.*;
import matteroverdrive.gui.element.android_station.ElementBioStat;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityAndroidStation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;

public class GuiAndroidStation extends MOGuiMachine<TileEntityAndroidStation> {

	ElementSlot[] parts_slots = new ElementSlot[Reference.BIONIC_BATTERY + 1];
	List<ElementBioStat> stats = new ArrayList<>(MatterOverdrive.STAT_REGISTRY.getStats().size());
	private EntityMeleeRougeAndroidMob mob;
	private MOElementButtonScaled hudConfigs;
	private ElementGroup2DScroll abilitiesGroup;

	public GuiAndroidStation(InventoryPlayer inventoryPlayer, TileEntityAndroidStation machine) {
		super(new ContainerAndroidStation(inventoryPlayer, machine), machine, 364, 250);
		texW = 255;
		texH = 237;
		AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(inventoryPlayer.player);

		background = GuiWeaponStation.BG;

		for (int i = 0; i < parts_slots.length; i++) {
			parts_slots[i] = new ElementInventorySlot(this, (MOSlot) inventorySlots.getSlot(i), 20, 20, "holo",
					androidPlayer.getInventory().getSlot(i).getHoloIcon());
			parts_slots[i].setColor(Reference.COLOR_MATTER.getIntR(), Reference.COLOR_MATTER.getIntG(),
					Reference.COLOR_MATTER.getIntB(), 78);
			parts_slots[i].setInfo("biopart." + BionicSlot.names[i] + ".name");
		}

		parts_slots[Reference.BIONIC_HEAD].setPosition(220, ySize - 110);
		parts_slots[Reference.BIONIC_ARMS].setPosition(220, ySize - 80);
		parts_slots[Reference.BIONIC_LEGS].setPosition(220, ySize - 50);

		parts_slots[Reference.BIONIC_CHEST].setPosition(320, ySize - 110);
		parts_slots[Reference.BIONIC_OTHER].setPosition(320, ySize - 80);
		parts_slots[Reference.BIONIC_BATTERY].setPosition(320, ySize - 50);
		parts_slots[Reference.BIONIC_BATTERY].setIcon(ClientProxy.holoIcons.getIcon("battery"));

		addStat(androidPlayer, OverdriveBioticStats.teleport, 0, 0, null);
		addStat(androidPlayer, OverdriveBioticStats.nanobots, 1, 1, null);
		addStat(androidPlayer, OverdriveBioticStats.nanoArmor, 0, 1, EnumFacing.EAST);
		addStat(androidPlayer, OverdriveBioticStats.flotation, 2, 0, null);
		addStat(androidPlayer, OverdriveBioticStats.speed, 3, 0, EnumFacing.SOUTH);
		addStat(androidPlayer, OverdriveBioticStats.highJump, 3, 1, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.equalizer, 3, 3, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.shield, 0, 2, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.attack, 2, 1, EnumFacing.WEST);
		addStat(androidPlayer, OverdriveBioticStats.cloak, 0, 3, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.nightvision, 1, 0, null);
		addStat(androidPlayer, OverdriveBioticStats.minimap, 4, 0, null);
		addStat(androidPlayer, OverdriveBioticStats.flashCooling, 2, 2, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.shockwave, 2, 3, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.autoShield, 1, 2, EnumFacing.WEST);
		addStat(androidPlayer, OverdriveBioticStats.stepAssist, 5, 0, null);
		addStat(androidPlayer, OverdriveBioticStats.zeroCalories, 4, 2, null);
		addStat(androidPlayer, OverdriveBioticStats.wirelessCharger, 1, 3, null);
		addStat(androidPlayer, OverdriveBioticStats.inertialDampers, 3, 2, EnumFacing.UP);
		addStat(androidPlayer, OverdriveBioticStats.itemMagnet, 5, 1, null);
		addStat(androidPlayer, OverdriveBioticStats.airDash, 4, 1, EnumFacing.WEST);
		addStat(androidPlayer, OverdriveBioticStats.oxygen, 4, 3, EnumFacing.UP);

		mob = new EntityMeleeRougeAndroidMob(Minecraft.getMinecraft().world);
		mob.getEntityData().setBoolean("Hologram", true);

		hudConfigs = new MOElementButtonScaled(this, this, 48, 64, "hud_configs", 128, 24);
		hudConfigs.setText("HUD Options");
	}

	public void addStat(AndroidPlayer androidPlayer, IBioticStat stat, int x, int y, EnumFacing direction,
			boolean strong) {
		ElementBioStat elemStat = new ElementBioStat(this, 0, 0, stat, androidPlayer.getUnlockedLevel(stat),
				androidPlayer, direction);
		elemStat.setStrongConnection(strong);
		elemStat.setPosition(54 + x * 30, 36 + y * 30);
		stats.add(elemStat);
	}

	public void addStat(AndroidPlayer androidPlayer, IBioticStat stat, int x, int y, EnumFacing direction) {
		addStat(androidPlayer, stat, x, y, direction, false);
	}

	@Override
	public void initGui() {
		super.initGui();

		pages.get(0).addElement(abilitiesGroup);

		for (ElementSlot elementSlot : parts_slots) {
			pages.get(0).addElement(elementSlot);
		}

		for (ElementBioStat stat : stats) {
			pages.get(0).addElement(stat);
		}
		pages.get(1).addElement(hudConfigs);

		AddMainPlayerSlots(inventorySlots, this);
		AddHotbarPlayerSlots(inventorySlots, this);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
		super.drawGuiContainerBackgroundLayer(partialTick, x, y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		if (pages.get(0).isVisible()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 300);
			drawEntityOnScreen(280, ySize - 25, 50, -this.mouseX + 280, -this.mouseY + ySize - 100, mc.player);
			GlStateManager.popMatrix();

			String info = Minecraft.getMinecraft().player.experienceLevel + " XP";
			GlStateManager.disableLighting();
			int width = fontRenderer.getStringWidth(info);
			fontRenderer.drawString(TextFormatting.GREEN + info, 280 - width / 2, ySize - 20, 0xFFFFFF);
		}
	}

	public void handleElementButtonClick(MOElementBase element, String elementName, int mouseButton) {
		super.handleElementButtonClick(element, elementName, mouseButton);
		if (element.equals(hudConfigs)) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig(this, ConfigurationHandler.CATEGORY_ANDROID_HUD));
		}
	}

	/**
	 * Draws an entity on the screen Copied from
	 * {@link net.minecraft.client.gui.inventory.GuiInventory}
	 *
	 * @param posX
	 * @param posY
	 * @param scale
	 * @param mouseX
	 * @param mouseY
	 * @param ent
	 */
	private void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) posX, (float) posY, 1f);
		GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
		GlStateManager.rotate(180.0F, 180.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(ent.world.getWorldTime(), 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		ent.renderYawOffset = 0;
		ent.rotationYaw = 0;
		ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
