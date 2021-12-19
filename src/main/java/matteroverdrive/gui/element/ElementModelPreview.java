
package matteroverdrive.gui.element;

import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ElementModelPreview extends MOElementBase {
    ItemStack itemStack;

    public ElementModelPreview(MOGuiBase gui, int posX, int posY) {
        super(gui, posX, posY);
    }

    public ElementModelPreview(MOGuiBase gui, int posX, int posY, int width, int height) {
        super(gui, posX, posY, width, height);
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void init() {

    }

    @Override
    public void addTooltip(List<String> var1, int mouseX, int mouseY) {

    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {

    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        if (itemStack != null) {
            GlStateManager.pushMatrix();
            Transform();
            GlStateManager.disableCull();
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.GUI);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    public void Transform() {
        GlStateManager.translate(posX, posY, 80);
        GlStateManager.rotate(-90, 0, 1, 0);
        GlStateManager.rotate(210, 0, 0, 1);
        GlStateManager.rotate(-25, 0, 1, 0);
        GlStateManager.scale(120, 120, 120);
        //GlStateManager.translate(0,-0.7,0);
        //GlStateManager.translate(0.2,0.2,0.2);
    }

    /*public IItemRenderer getRenderer() {
		return renderer;
    }

    public void setRenderer(IItemRenderer renderer) {
        this.renderer = renderer;
    }*/

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
