
package matteroverdrive.gui.element.list;

import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.gui.element.IMOListBoxElement;
import matteroverdrive.gui.element.MOElementListBox;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ListElementQuest implements IMOListBoxElement {
    private QuestStack questStack;
    private EntityPlayer entityPlayer;
    private int width;

    public ListElementQuest(EntityPlayer entityPlayer, QuestStack questStack, int width) {
        this.questStack = questStack;
        this.entityPlayer = entityPlayer;
        this.width = width;
    }

    @Override
    public String getName() {
        return questStack.getTitle(entityPlayer);
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 6;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public Object getValue() {
        return questStack;
    }

    @Override
    public void draw(MOElementListBox listBox, int x, int y, int backColor, int textColor, boolean selected, boolean BG) {

        int textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(getName());
        if (selected) {
            listBox.getFontRenderer().drawString("\u2023 " + getName(), x + width / 2 - textWidth / 2 - 8, y, textColor);
        } else {
            listBox.getFontRenderer().drawString(getName(), x + width / 2 - textWidth / 2, y, textColor);
        }
    }

    @Override
    public void drawToolTop(MOElementListBox listBox, int x, int y) {

    }
}
