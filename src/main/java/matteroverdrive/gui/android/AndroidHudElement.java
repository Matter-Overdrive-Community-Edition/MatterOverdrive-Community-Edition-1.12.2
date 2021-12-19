
package matteroverdrive.gui.android;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class AndroidHudElement implements IAndroidHudElement {
    protected Minecraft mc;
    protected String name;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected Color baseColor;
    protected float backgroundAlpha;
    protected AndroidHudPosition defaultPosition;
    protected AndroidHudPosition hudPosition;

    public AndroidHudElement(AndroidHudPosition defaultPosition, String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        mc = Minecraft.getMinecraft();
        baseColor = Reference.COLOR_HOLO;
        hudPosition = this.defaultPosition = defaultPosition;
    }

    @Override
    public int getWidth(ScaledResolution resolution, AndroidPlayer androidPlayer) {
        return width;
    }

    @Override
    public int getHeight(ScaledResolution resolution, AndroidPlayer androidPlayer) {
        return height;
    }

    @Override
    public void setX(int x) {
        this.posX = x;
    }

    @Override
    public void setY(int y) {
        this.posY = y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setBaseColor(Color color) {
        this.baseColor = color;
    }

    public AndroidHudPosition getPosition() {
        return this.hudPosition;
    }

    public void setHudPosition(AndroidHudPosition position) {
        this.hudPosition = position;
    }

    public AndroidHudPosition getDefaultPosition() {
        return defaultPosition;
    }

    public void setBackgroundAlpha(float alpha) {
        this.backgroundAlpha = alpha;
    }
}
