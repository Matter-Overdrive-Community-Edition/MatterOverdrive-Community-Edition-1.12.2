
package matteroverdrive.guide;

import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

public class GuideElementImage extends GuideElementAbstract {
    ResourceLocation location;
    int imageWidth;
    int imageHeight;

    @Override
    public void drawElement(int width, int mouseX, int mouseY) {
        int maxWidth = width - marginLeft - marginRight;
        float imageScale = (float) maxWidth / (float) Math.max(imageWidth, maxWidth);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //RenderUtils.applyColor(Reference.COLOR_HOLO_YELLOW);
        //RenderUtils.drawPlane(x + marginLeft - 1, marginTop - 1, 0, imageWidth * imageScale + 2, imageHeight * imageScale + 2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (location != null) {
            RenderUtils.applyColor(color);
            bindTexture(location);
            int x = 0;
            if (textAlign == 1) {
                x = width / 2 - (int) (imageWidth * imageScale / 2);
            }
            RenderUtils.drawPlane(x + marginLeft, marginTop, 0, imageWidth * imageScale, imageHeight * imageScale);
        }
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
        if (element.hasAttribute("src")) {
            location = new ResourceLocation(Reference.PATH_GFX + element.getAttribute("src"));
        }
        if (element.hasAttribute("width")) {
            imageWidth = Integer.parseInt(element.getAttribute("width"));
        }
        if (element.hasAttribute("height")) {
            imageHeight = Integer.parseInt(element.getAttribute("height"));
        }

        this.height = imageHeight;
        this.width = imageWidth;
    }
}
