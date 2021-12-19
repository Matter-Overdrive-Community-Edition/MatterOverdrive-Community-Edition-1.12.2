
package matteroverdrive.client.model;

import matteroverdrive.client.data.Color;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class MOModelRenderColored extends ModelRenderer {
    boolean disableLighting;
    Color color;

    public MOModelRenderColored(ModelBase p_i1174_1_, int p_i1174_2_, int p_i1174_3_) {
        super(p_i1174_1_, p_i1174_2_, p_i1174_3_);
    }

    @SideOnly(Side.CLIENT)
    public void render(float p_78785_1_) {
        if (disableLighting) {
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderUtils.disableLightmap();
        }

        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        RenderUtils.applyColor(color);
        super.render(p_78785_1_);
        GL11.glPopAttrib();
        if (disableLighting) {
            GL11.glEnable(GL11.GL_LIGHTING);
            RenderUtils.enableLightmap();
        }
    }

    public void setDisableLighting(boolean disableLighting) {
        this.disableLighting = disableLighting;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
