
package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ElementGuideEntry extends MOElementBase {
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "quide_element_bg.png");
    private final MOGuideEntry entry;
    private final IButtonHandler buttonHandler;
    private boolean showLabel;

    public ElementGuideEntry(MOGuiBase gui, IButtonHandler buttonHandler, int posX, int posY, MOGuideEntry entry) {
        super(gui, posX, posY);
        this.entry = entry;
        this.setSize(22, 22);
        this.buttonHandler = buttonHandler;
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void init() {

    }

    @Override
    public void addTooltip(List<String> list, int mouseX, int mouseY) {
        list.add(entry.getDisplayName());
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        GlStateManager.color(1, 1, 1);
        gui.bindTexture(BG);
        gui.drawSizedTexturedModalRect(this.posX, this.posY, 0, 0, 22, 22, 22, 22);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        int iconIndex = (int) ((Minecraft.getMinecraft().world.getWorldTime() / 20) % entry.getStackIcons().length);
        RenderUtils.renderStack(this.posX + 3, this.posY + 3, entry.getStackIcons()[iconIndex]);

        if (showLabel) {
            getFontRenderer().setUnicodeFlag(true);
            getFontRenderer().drawString(entry.getDisplayName(), this.posX + sizeX + 4, this.posY + this.sizeY / 2 - 5, Reference.COLOR_MATTER.getColor());
            getFontRenderer().setUnicodeFlag(false);
        }
    }

    public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
        buttonHandler.handleElementButtonClick(this, name, mouseButton);
        return true;
    }

    public MOGuideEntry getEntry() {
        return entry;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }
}
