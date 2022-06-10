
package matteroverdrive.starmap.gen;

import java.util.Random;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PlanetAbstractGen implements ISpaceBodyGen<Planet> {
	byte type;
	int buildingSpaces, fleetSpaces;

	public PlanetAbstractGen(byte type, int buildingSpaces, int fleetSpaces) {
		this.type = type;
		this.buildingSpaces = buildingSpaces;
		this.fleetSpaces = fleetSpaces;
	}

	@Override
	public void generateSpaceBody(Planet planet, Random random) {
		planet.setType((byte) 2);
		setSize(planet, random);
	}

	@Override
	public boolean generateMissing(NBTTagCompound tagCompound, Planet planet, Random random) {
		if (planet.getType() == type) {
			if (!tagCompound.hasKey("Type", 1)) {
				planet.setType(type);
			}
			if (!tagCompound.hasKey("Size", 5)) {
				setSize(planet, random);
			}
			return true;
		}
		return false;
	}

	protected abstract void setSize(Planet planet, Random random);
}
