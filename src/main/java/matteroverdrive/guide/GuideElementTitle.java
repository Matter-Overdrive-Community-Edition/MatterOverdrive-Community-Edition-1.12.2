
package matteroverdrive.guide;

import net.minecraft.client.renderer.GlStateManager;
import org.w3c.dom.Element;

public class GuideElementTitle extends GuideElementTextAbstract {
    String title;
    double size = 3;

    @Override
    public void drawElement(int width, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        int titleWidth = (int) (getFontRenderer().getStringWidth(title) * size);
        int x = 0;
        if (textAlign == 1) {
            x = (width - marginLeft - marginRight) / 2 - titleWidth / 2;
        } else if (textAlign == 2) {
            x = (width - marginLeft - marginRight) - titleWidth;
        }
        GlStateManager.translate(x + marginLeft, marginTop, 0);
        GlStateManager.scale(size, size, size);
        getFontRenderer().drawString(title, 0, 0, color.getColor());
        GlStateManager.popMatrix();
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
        title = handleVariables(element.getTextContent(), entry);
        if (element.hasAttribute("size")) {
            size = Double.parseDouble(element.getAttribute("size"));
        }

        this.width = width;
        this.height = (int) (getFontRenderer().FONT_HEIGHT * size);
    }
}
