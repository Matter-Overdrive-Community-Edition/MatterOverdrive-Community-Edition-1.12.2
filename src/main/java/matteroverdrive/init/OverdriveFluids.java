
package matteroverdrive.init;

import matteroverdrive.fluids.FluidMatterPlasma;
import matteroverdrive.fluids.FluidMoltenTritanium;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class OverdriveFluids {
	public static FluidMatterPlasma matterPlasma;
	public static FluidMoltenTritanium moltenTritanium;

	public static void init(FMLPreInitializationEvent event) {
		matterPlasma = new FluidMatterPlasma("matter_plasma");
		FluidRegistry.registerFluid(matterPlasma);
		FluidRegistry.addBucketForFluid(matterPlasma);

		moltenTritanium = new FluidMoltenTritanium("molten_tritanium");
		FluidRegistry.registerFluid(moltenTritanium);
		FluidRegistry.addBucketForFluid(moltenTritanium);

	}
}
