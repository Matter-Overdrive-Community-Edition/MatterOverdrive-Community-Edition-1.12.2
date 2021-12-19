
package matteroverdrive.init;

import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.world.DimensionalRifts;
import matteroverdrive.world.MOWorldGen;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class MatterOverdriveWorld {
    public final MOWorldGen worldGen;
    private final DimensionalRifts dimensionalRifts;

    public MatterOverdriveWorld(ConfigurationHandler configurationHandler) {
        worldGen = new MOWorldGen();
        dimensionalRifts = new DimensionalRifts(1);
        configurationHandler.subscribe(worldGen);
    }

    public void init(ConfigurationHandler configurationHandler) {
        worldGen.init(configurationHandler);
        GameRegistry.registerWorldGenerator(worldGen, 0);
    }

    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side.equals(Side.SERVER)) {
            worldGen.manageBuildingGeneration();
        }
    }

    public DimensionalRifts getDimensionalRifts() {
        return dimensionalRifts;
    }
}
