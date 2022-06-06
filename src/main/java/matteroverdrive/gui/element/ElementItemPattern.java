package matteroverdrive.gui.element;

import java.util.List;

import org.lwjgl.input.Keyboard;

import matteroverdrive.data.ScaleTexture;
import matteroverdrive.data.matter_network.ItemPatternMapping;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ElementItemPattern extends ElementSlot {
    protected ScaleTexture texture;
    protected ItemPatternMapping patternMapping;
    protected ItemStack itemStack;
    protected int amount = 0;

    public ElementItemPattern(MOGuiBase gui, ItemPatternMapping patternMapping, String bgType, int width, int height) {
        super(gui, 0, 0, width, height, bgType);
        this.texture = new ScaleTexture(getTexture(bgType), width, height).setOffsets(2, 2, 2, 2);
        this.setPatternMapping(patternMapping);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        if (patternMapping != null) {
            itemStack.setCount(amount);
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            RenderUtils.renderStack(posX + 3, posY + 3, 100, itemStack, true);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        texture.render(posX, posY, sizeX, sizeY);
    }

    @Override
    public void addTooltip(List<String> list, int mouseX, int mouseY) {
        if (patternMapping != null) {
            if (itemStack != null) {
                list.addAll(itemStack.getTooltip(Minecraft.getMinecraft().player, ITooltipFlag.TooltipFlags.NORMAL));
                String name = list.get(0);
                int matterValue = MatterHelper.getMatterAmountFromItem(itemStack);
                String matter = TextFormatting.BLUE + MOStringHelper.translateToLocal("gui.tooltip.matter") + ": " + TextFormatting.GOLD + MatterHelper.formatMatter(matterValue);
                int progress = patternMapping.getItemPattern().getProgress();
                name = MatterDatabaseHelper.getPatternInfoColor(progress) + name + " [" + progress + "%]";
                list.set(0, name);
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                	
                } else {
                list.set(1, matter);
                }
            }
        }
    }

    public ItemPatternMapping getPatternMapping() {
        return patternMapping;
    }

    public void setPatternMapping(ItemPatternMapping patternMapping) {
        this.patternMapping = patternMapping;
        if (patternMapping != null) {
            itemStack = patternMapping.getItemPattern().toItemStack(false);
            this.name = itemStack.getDisplayName();
        } else {
            itemStack = null;
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ScaleTexture getTexture() {
        return texture;
    }
}
