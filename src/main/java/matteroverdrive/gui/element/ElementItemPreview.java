
package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ElementItemPreview extends MOElementBase {
    ScaleTexture background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "item_preview_bg.png"), 40, 48).setOffsets(22, 22, 18, 18);
    ItemStack itemStack;
    float itemSize = 2;
    boolean renderOverlay;
    boolean drawTooltip;

    public ElementItemPreview(MOGuiBase gui, int posX, int posY, ItemStack itemStack) {
        super(gui, posX, posY);
        this.sizeX = 47;
        this.sizeY = 47;
        this.itemStack = itemStack;
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void init() {

    }

    @Override
    public void addTooltip(List<String> tooltip, int mouseX, int mouseY) {
        tooltip.addAll(itemStack.getTooltip(Minecraft.getMinecraft().player, Minecraft.getMinecraft().gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL));
    }

    public void setItemSize(float itemSize) {
        this.itemSize = itemSize;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        if (background != null) {
            background.render(posX, posY, sizeX, sizeY);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        if (itemStack != null) {
            GlStateManager.pushMatrix();
            GL11.glTranslatef(this.posX + sizeX / 2 - 9 * itemSize, this.posY + sizeY / 2 - 9 * itemSize, 0);
            GlStateManager.scale(itemSize, itemSize, itemSize);
            RenderUtils.renderStack(0, 0, 32, itemStack, renderOverlay);
            GlStateManager.popMatrix();
        }
    }

    public void setRenderOverlay(boolean renderOverlay) {
        this.renderOverlay = renderOverlay;
    }

    public void setDrawTooltip(boolean drawTooltip) {
        this.drawTooltip = drawTooltip;
    }

    public void setBackground(ScaleTexture background) {
        this.background = background;
    }
}
