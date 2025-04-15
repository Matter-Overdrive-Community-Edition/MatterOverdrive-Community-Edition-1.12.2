
package matteroverdrive.gui.element.android_station;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import java.util.List;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.network.packet.server.PacketUnlockBioticStat;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class ElementBioStat extends MOElementButton {
	private final IBioticStat stat;
	private final AndroidPlayer player;
	private int level;
	private EnumFacing direction;
	private boolean strongConnection;
	private final ResourceLocation strongConnectionTex = new ResourceLocation(Reference.PATH_ELEMENTS + "connection.png");
	private final ResourceLocation strongConnectionBrokenTex = new ResourceLocation(
			Reference.PATH_ELEMENTS + "connection_broken.png");

	public ElementBioStat(MOGuiBase gui, int posX, int posY, IBioticStat stat, int level, AndroidPlayer player, EnumFacing direction) {
		super(gui, gui, posX, posY, stat.getUnlocalizedName(), 0, 0, 0, 0, 22, 22, "");
		texture = ElementSlot.getTexture("holo");
		texW = 22;
		texH = 22;
		this.stat = stat;
		this.player = player;
		this.level = level;
		this.direction = direction;
	}

	@Override
	public boolean isEnabled() {
		if (stat.canBeUnlocked(player, level)) {
			if (player.getUnlockedLevel(stat) < stat.maxLevel()) {
				return true;
			}
		}
		return false;
	}

	protected void ApplyColor() {
		if (stat.canBeUnlocked(player, level) || player.isUnlocked(stat, level)) {
			if (level <= 0) {
				RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
			} else {
				RenderUtils.applyColor(Reference.COLOR_HOLO);
			}
		} else {
			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO_RED, 0.5f);
		}
	}

	protected void ResetColor() {
		GlStateManager.color(1, 1, 1);
	}

	@Override
	public void addTooltip(List<String> list, int mouseX, int mouseY) {
		stat.onTooltip(player, MathHelper.clamp(level, 0, stat.maxLevel()), list, mouseX, mouseY);
	}

	@Override
	public void onAction(int mouseX, int mouseY, int mouseButton) {
		if (super.intersectsWith(mouseX, mouseY)) {
			if (stat.canBeUnlocked(player, level + 1) && level < stat.maxLevel()) {
				MOGuiBase.playSound(MatterOverdriveSounds.guiBioticStatUnlock, 1, 1);
				MatterOverdrive.NETWORK.sendToServer(new PacketUnlockBioticStat(stat.getUnlocalizedName(), ++level));
			}
		}
		super.onAction(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
		ApplyColor();
		this.gui.drawSizedTexturedModalRect(var1, var2, var3, var4, var5, var6, (float) this.texW, (float) this.texH);
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		GlStateManager.enableBlend();
		ApplyColor();
		super.drawBackground(mouseX, mouseY, gameTicks);
		drawIcon(stat.getIcon(level), posX + 3, posY + 3);
		if (direction != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, 0);
			GlStateManager.translate(sizeX / 2, sizeY / 2, 0);
			GlStateManager.translate(direction.getXOffset() * (sizeX * 0.75), -direction.getYOffset() * (sizeY * 0.75),
					0);
			if (direction == EnumFacing.EAST) {
				GlStateManager.rotate(90, 0, 0, 1);
			} else if (direction == EnumFacing.WEST) {
				GlStateManager.rotate(-90, 0, 0, 1);
			} else if (direction == EnumFacing.DOWN) {
				GlStateManager.rotate(180, 0, 0, 1);
			}
			if (strongConnection) {
				GlStateManager.translate(-3.5, -26, 0);
				if (stat.isLocked(player, level)) {
					RenderUtils.bindTexture(strongConnectionBrokenTex);
				} else {
					RenderUtils.bindTexture(strongConnectionTex);
				}
				RenderUtils.drawPlane(7, 30);
			} else {
				GlStateManager.translate(-3.5, -3.5, 0);
				ClientProxy.holoIcons.renderIcon("up_arrow", 0, 0);
			}
			GlStateManager.popMatrix();
		}
		ResetColor();
		GlStateManager.disableBlend();
	}

	public void drawForeground(int x, int y) {
		if (stat.maxLevel() > 1 && level > 0) {
			String levelInfo = Integer.toString(level);
			ClientProxy.holoIcons.renderIcon("black_circle", posX + 14, posY + 14, 10, 10);
			getFontRenderer().drawString(levelInfo, posX + 16, posY + 16, 0xFFFFFF);
		}
	}

	public void drawIcon(HoloIcon icon, int x, int y) {
		if (icon != null) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			ClientProxy.holoIcons.renderIcon(icon, x, y, 16, 16);
			GlStateManager.disableBlend();
		}
	}

	public IBioticStat getStat() {
		return stat;
	}

	public void setStrongConnection(boolean strongConnection) {
		this.strongConnection = strongConnection;
	}

	public EnumFacing getDirection() {
		return direction;
	}

	public void setDirection(EnumFacing direction) {
		this.direction = direction;
	}
}
