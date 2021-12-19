
package matteroverdrive.gui.element;

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ElementStates extends MOElementButtonScaled {
    String[] states;
    int selectedState;
    String label;

    public ElementStates(MOGuiBase gui, IButtonHandler buttonHandler, int posX, int posY, String name, String[] states) {
        super(gui, buttonHandler, posX, posY, name, 0, 0);
        this.name = name;
        this.states = states;
        this.sizeY = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 10;
        for (String state : states) {
            if (this.sizeX < Minecraft.getMinecraft().fontRenderer.getStringWidth(state)) {
                this.sizeX = Minecraft.getMinecraft().fontRenderer.getStringWidth(state);
            }
        }
        this.sizeX += 16;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public void setSelectedState(int selectedState) {
        this.selectedState = selectedState;
        this.text = states[selectedState];
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.5f);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        getFontRenderer().drawString(label, posX + sizeX + 4, posY - getFontRenderer().FONT_HEIGHT / 2 + sizeY / 2, getTextColor());
    }

    @Override
    public void onAction(int mouseX, int mouseY, int mouseButton) {
        selectedState++;
        if (selectedState >= states.length) {
            selectedState = 0;
        }

        if (selectedState < states.length) {
            text = states[selectedState];
        }
        buttonHandler.handleElementButtonClick(this, name, selectedState);
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
