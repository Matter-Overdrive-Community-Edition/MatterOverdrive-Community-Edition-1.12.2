
package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class MatterConnectionElement extends MOElementBase {
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ELEMENTS + "side_slot_bg.png");

    int id;
    int count;

    public MatterConnectionElement(MOGuiBase gui, int id, int count) {
        this(gui, 22, 22, id, count);
    }

    public MatterConnectionElement(MOGuiBase gui, int width, int height, int id, int count) {
        super(gui, 0, 0, width, height);

        this.id = id;
        this.count = count;
    }

    @Override
    public void addTooltip(List<String> list, int mouseX, int mouseY) {
        list.add(MOStringHelper.translateToLocal(Item.getItemById(id).getTranslationKey() + ".name") + " [" + count + "]");
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void init() {

    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {
        GlStateManager.color(1, 1, 1);
        RenderUtils.bindTexture(texture);
        gui.drawSizedTexturedModalRect(posX, posY, 0, 0, 22, 22, 22, 22);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        RenderUtils.renderStack(posX + 2, posY + 2, new ItemStack(Item.getItemById(id)));
        gui.getFontRenderer().drawStringWithShadow(Integer.toString(count), posX + 8, posY + 24, 0xFFFFFF);
    }
}
