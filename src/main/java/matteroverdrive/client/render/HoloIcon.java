
package matteroverdrive.client.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HoloIcon {
    private TextureAtlasSprite icon;
    private int originalWidth;
    private int originalHeight;

    public HoloIcon(TextureAtlasSprite icon, int originalX, int originalY) {
        this.icon = icon;
        setOriginalSize(originalX, originalY);
    }

    public void setOriginalSize(int originalX, int originalY) {
        this.originalWidth = originalX;
        this.originalHeight = originalY;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public TextureAtlasSprite getIcon() {
        return icon;
    }

    public void setIcon(TextureAtlasSprite icon) {
        this.icon = icon;
    }
}
