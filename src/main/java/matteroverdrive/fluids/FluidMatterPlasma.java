
package matteroverdrive.fluids;

import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidMatterPlasma extends Fluid {
    public FluidMatterPlasma(String fluidName) {
        super(fluidName, new ResourceLocation(Reference.MOD_ID, "fluids/matter_plasma/still"), new ResourceLocation(Reference.MOD_ID, "fluids/matter_plasma/flowing"));
        setViscosity(8000);
        setLuminosity(15);
    }
}
