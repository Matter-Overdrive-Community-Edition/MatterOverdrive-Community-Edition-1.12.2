
package matteroverdrive.starmap.gen;

import java.util.Random;

import matteroverdrive.starmap.data.Planet;

public class PlanetDwarfGen extends PlanetAbstractGen {
	public PlanetDwarfGen() {
		super((byte) 2, 4, 4);
	}

	@Override
	protected void setSize(Planet planet, Random random) {
		planet.setSize(0.2f + random.nextFloat() * 0.4f);
	}

	@Override
	public double getWeight(Planet planet) {
		if (planet.getOrbit() > 0.6f || planet.getOrbit() < 0.4f) {
			return 0.3f;
		}
		return 0.1f;
	}
}
