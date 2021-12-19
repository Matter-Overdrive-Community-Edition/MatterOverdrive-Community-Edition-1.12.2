
package matteroverdrive.api.starmap;

public enum PlanetStatType {
    FLEET_SIZE("fleet_size"),
    BUILDINGS_SIZE("building_size"),
    SHIP_BUILD_SPEED("ship_build_speed"),
    BUILDING_BUILD_SPEED("building_build_speed"),
    MATTER_STORAGE("matter_storage"),
    ENERGY_PRODUCTION("energy_production"),
    POPULATION_COUNT("population_count"),
    HAPPINESS("happiness"),
    MATTER_PRODUCTION("matter_production");

    private final String unlocalizedName;

    PlanetStatType(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}
