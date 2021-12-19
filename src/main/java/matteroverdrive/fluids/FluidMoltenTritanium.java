
package matteroverdrive.fluids;

import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidMoltenTritanium extends Fluid {
    public FluidMoltenTritanium(String fluidName) {
        super(fluidName, new ResourceLocation(Reference.MOD_ID, "fluids/molten_tritanium/still"), new ResourceLocation(Reference.MOD_ID, "fluids/molten_tritanium/flowing"));
        setViscosity(6000);
        setLuminosity(15);
        setTemperature(2000);
    }
}
