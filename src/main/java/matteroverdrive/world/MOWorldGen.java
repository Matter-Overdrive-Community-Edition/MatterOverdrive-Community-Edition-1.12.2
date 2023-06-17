
package matteroverdrive.world;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.Platform;
import matteroverdrive.world.buildings.*;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.*;

public class MOWorldGen implements IWorldGenerator, IConfigSubscriber {
	private static float BUILDING_SPAWN_CHANCE = 0.01f;
	private static final int TRITANIUM_VEINS_PER_CHUNK = 5;
	private static final int TRITANIUM_VEIN_SIZE = 4;
	private static final int DILITHIUM_VEINS_PER_CHUNK = 2;
	private static final int DILITHIUM_VEIN_SIZE = 3;
	public final List<WeightedRandomMOWorldGenBuilding> buildings;
	private final Random oreRandom;
	private final Random anomaliesRandom;
	private final Random buildingsRandom;
	HashSet<Integer> oreDimentionsBlacklist;
	HashSet<Integer> buildingsDimentionsBlacklist;
	boolean generateTritanium;
	boolean generateDilithium;
	boolean generateAnomalies;
	boolean generateBuildings = true;
	private int MOAndroidHouseBuildingchance = 20;
	private int MOSandPitchance = 100;
	private int MOWorldGenCrashedSpaceShipchance = 60;
	private int MOWorldGenUnderwaterBasechance = 20;
	private int MOWorldGenCargoShipchance = 5;

	private WorldGenMinable dilithiumGen;
	private WorldGenMinable tritaniumGen;
	private WorldGenGravitationalAnomaly anomalyGen;
	private Queue<MOImageGen.ImageGenWorker> worldGenBuildingQueue;

	public MOWorldGen() {
		oreRandom = new Random();
		anomaliesRandom = new Random();
		buildingsRandom = new Random();
		buildings = new ArrayList<>();
		worldGenBuildingQueue = new ArrayDeque<>();
		oreDimentionsBlacklist = new HashSet<>();
		buildingsDimentionsBlacklist = new HashSet<>();
	}

	public static GenPositionWorldData getWorldPositionData(World world) {
		GenPositionWorldData data = (GenPositionWorldData) world.loadData(GenPositionWorldData.class,
				Reference.WORLD_DATA_MO_GEN_POSITIONS);
		if (data == null) {
			data = new GenPositionWorldData(Reference.WORLD_DATA_MO_GEN_POSITIONS);
			world.setData(Reference.WORLD_DATA_MO_GEN_POSITIONS, data);
		}
		return data;
	}

	public void init(ConfigurationHandler configurationHandler) {
		tritaniumGen = new WorldGenMinable(MatterOverdrive.BLOCKS.tritaniumOre.getDefaultState(), TRITANIUM_VEIN_SIZE);
		dilithiumGen = new WorldGenMinable(MatterOverdrive.BLOCKS.dilithium_ore.getDefaultState(), DILITHIUM_VEIN_SIZE);

		buildings.add(new WeightedRandomMOWorldGenBuilding(new MOAndroidHouseBuilding("android_house"),
				MOAndroidHouseBuildingchance));
		buildings.add(new WeightedRandomMOWorldGenBuilding(new MOSandPit("sand_pit_house", 3), MOSandPitchance));
		buildings.add(new WeightedRandomMOWorldGenBuilding(new MOWorldGenCrashedSpaceShip("crashed_ship"),
				MOWorldGenCrashedSpaceShipchance));
		buildings.add(new WeightedRandomMOWorldGenBuilding(new MOWorldGenUnderwaterBase("underwater_base"),
				MOWorldGenUnderwaterBasechance));
		buildings.add(
				new WeightedRandomMOWorldGenBuilding(new MOWorldGenCargoShip("cargo_ship"), MOWorldGenCargoShipchance));
		buildings.add(new WeightedRandomMOWorldGenBuilding(new MOAdvFusion("advfusion"), 0));
		buildings.add(new WeightedRandomMOWorldGenBuilding(new MOFusion("fusion"), 0));

		anomalyGen = new WorldGenGravitationalAnomaly("gravitational_anomaly", 0.005f, 2048, 2048 + 8192);
		configurationHandler.subscribe(anomalyGen);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		long worldSeed = world.getSeed();
		Random moRandom = new Random(worldSeed);
		long xSeed = moRandom.nextLong() >> 2 + 1L;
		long zSeed = moRandom.nextLong() >> 2 + 1L;
		long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
		oreRandom.setSeed(chunkSeed);
		anomaliesRandom.setSeed(chunkSeed);
		buildingsRandom.setSeed(chunkSeed);
		generateGravitationalAnomalies(world, anomaliesRandom, chunkX * 16, chunkZ * 16);
		generateOres(world, oreRandom, chunkX * 16, chunkZ * 16, world.provider.getDimension());
		startGenerateBuildings(world, buildingsRandom, chunkX, chunkZ, chunkGenerator, chunkProvider, world.provider.getDimension());
	}

	public void generateOres(World world, Random random, int chunkX, int chunkZ, int dimentionID) {
		if (!oreDimentionsBlacklist.contains(dimentionID)) {
			if (generateDilithium) {
				for (int i = 0; i < DILITHIUM_VEINS_PER_CHUNK; i++) {
					int x = chunkX + random.nextInt(16);
					int z = chunkZ + random.nextInt(16);
					int y = random.nextInt(28) + 4;

					dilithiumGen.generate(world, random, new BlockPos(x, y, z));
				}
			}

			if (generateTritanium) {
				for (int i = 0; i < TRITANIUM_VEINS_PER_CHUNK; i++) {
					int x = chunkX + random.nextInt(16);
					int z = chunkZ + random.nextInt(16);
					int y = random.nextInt(60) + 4;

					tritaniumGen.generate(world, random, new BlockPos(x, y, z));
				}
			}
		}
	}

	private void generateGravitationalAnomalies(World world, Random random, int chunkX, int chunkZ) {
		if (generateAnomalies) {
			int y = anomalyGen.yLevelMap.getOrDefault(world.provider.getDimension(), world.getSeaLevel());
			BlockPos pos = new BlockPos(chunkX + random.nextInt(16),
					Math.min(y + random.nextInt(Math.min(255 - y, 60)) + 4, 255), chunkZ + random.nextInt(16));

			if (anomalyGen.generate(world, random, pos) && Platform.isDev())
				MOLog.debug("Generated Anomaly at %s", pos);
		}
	}

	private boolean shouldGenerate(Block block, ConfigurationHandler config) {
		Property p = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN,
				ConfigurationHandler.CATEGORY_WORLD_SPAWN + "." + block.getTranslationKey(), true);
		p.setLanguageKey(block.getTranslationKey() + ".name");
		return p.getBoolean(true);
	}

	public void startGenerateBuildings(World world, Random random, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, int dimentionID) {
		if (!buildingsDimentionsBlacklist.contains(dimentionID)) {
		if (generateBuildings && random.nextDouble() <= BUILDING_SPAWN_CHANCE) {
			BlockPos pos = world
					.getHeight(new BlockPos(chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16)));

			WeightedRandomMOWorldGenBuilding building = getRandomBuilding(world, pos.add(0, 0, 0), random);
			if (building != null) {
				startBuildingGeneration(building.worldGenBuilding, pos.add(0, 0, 0), random, world, chunkGenerator,
						chunkProvider, false);
			}
		}
	}
}
	public MOImageGen.ImageGenWorker startBuildingGeneration(MOWorldGenBuilding<?> building, BlockPos pos,
			Random random, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider,
			boolean forceGeneration) {
		if (building == null)
			return null;
		if ((forceGeneration || (building.shouldGenerate(random, world, pos)))) {
			MOImageGen.ImageGenWorker worker = building.createWorker(random, pos, world, chunkGenerator, chunkProvider);
			worldGenBuildingQueue.add(worker);
			MOLog.debug("Successful Generation of: %s at: %s", building, pos);
			return worker;
		}
		MOLog.debug("Failed Generation of: %s at: %s", building, pos);
		return null;
	}

	public void manageBuildingGeneration() {
		MOImageGen.ImageGenWorker worker = worldGenBuildingQueue.peek();
		if (worker != null && worker.generate()) {
			worldGenBuildingQueue.remove();
		}
	}

	public WeightedRandomMOWorldGenBuilding getRandomBuilding(World world, BlockPos pos, Random random) {
		return getBuilding(random, world, pos, buildings,
				random.nextInt(getTotalBuildingsWeight(random, world, pos, buildings)));
	}

	public int getTotalBuildingsWeight(Random random, World world, BlockPos pos,
			Collection<WeightedRandomMOWorldGenBuilding> collection) {
		int i = 0;
		WeightedRandomMOWorldGenBuilding building;

		for (Iterator<WeightedRandomMOWorldGenBuilding> iterator = collection.iterator(); iterator
				.hasNext(); i += building.getWeight(random, world, pos)) {
			building = (WeightedRandomMOWorldGenBuilding) iterator.next();

		}

		return i;
	}

	public WeightedRandomMOWorldGenBuilding getBuilding(Random random, World world, BlockPos pos,
			Collection<WeightedRandomMOWorldGenBuilding> par1Collection, int weight) {
		int j = weight;
		Iterator<WeightedRandomMOWorldGenBuilding> iterator = par1Collection.iterator();
		WeightedRandomMOWorldGenBuilding building;

		do {
			if (!iterator.hasNext()) {
				return null;
			}

			building = (WeightedRandomMOWorldGenBuilding) iterator.next();
			j -= building.getWeight(random, world, pos);
		} while (j >= 0);

		return building;
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config) {
		String comment = "Should Matter Overdrive Villager House be Generated ?";
		MadScientistHouse.generateBuilding = config.getBool(ConfigurationHandler.KEY_HOUSE, ConfigurationHandler.CATEGORY_WORLD_GEN,
				true, comment);
		config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.KEY_HOUSE, true)
				.setComment(comment);
		Property shouldGenerateOres = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN,
				ConfigurationHandler.CATEGORY_WORLD_SPAWN_ORES, true);
		shouldGenerateOres.setComment("Should Matter Overdrive Ore Blocks be Generated ?");
		generateTritanium = shouldGenerate(MatterOverdrive.BLOCKS.tritaniumOre, config)
				&& shouldGenerateOres.getBoolean(true);
		generateDilithium = shouldGenerate(MatterOverdrive.BLOCKS.dilithium_ore, config)
				&& shouldGenerateOres.getBoolean(true);
		Property shouldGenerateOthers = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN,
				ConfigurationHandler.CATEGORY_WORLD_SPAWN_OTHER, true);
		shouldGenerateOthers.setComment("Should other Matter Overdrive World Blocks be Generated?");
		generateBuildings = config.getBool("generate buildings", ConfigurationHandler.CATEGORY_WORLD_GEN, true,
				"Should Matter Overdrive Structures Generate aka ImageGen");
		MOAndroidHouseBuildingchance = (int) config.config.getInt(ConfigurationHandler.KEY_ANDROID_HOUSE_SPAWN_CHANCE,
				ConfigurationHandler.CATEGORY_WORLD_GEN, 20, 0, 100, "Spawn Weight of Android house");
		MOSandPitchance = (int) config.config.getInt(ConfigurationHandler.KEY_SAND_PIT_SPAWN_CHANCE,
				ConfigurationHandler.CATEGORY_WORLD_GEN, 100, 0, 100, "Spawn Weight of Sand pit");
		MOWorldGenCrashedSpaceShipchance = (int) config.config.getInt(
				ConfigurationHandler.KEY_CRASHED_SHIP_SPAWN_CHANCE, ConfigurationHandler.CATEGORY_WORLD_GEN, 60, 0, 100,
				"Spawn Weight of Crashed ship");
		MOWorldGenUnderwaterBasechance = (int) config.config.getInt(
				ConfigurationHandler.KEY_UNDERWATER_BASE_SPAWN_CHANCE, ConfigurationHandler.CATEGORY_WORLD_GEN, 20, 0,
				100, "Spawn Weight of Underwater base");
		MOWorldGenCargoShipchance = (int) config.config.getInt(ConfigurationHandler.KEY_CARGO_SHIP_SPAWN_CHANCE,
				ConfigurationHandler.CATEGORY_WORLD_GEN, 5, 0, 100, "Spawn Weight of Cargo ship");
		generateAnomalies = shouldGenerate(MatterOverdrive.BLOCKS.gravitational_anomaly, config);
		this.oreDimentionsBlacklist.clear();
		Property oreDimentionBlacklistProp = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN,
				"ore_gen_blacklist", new int[] { -1 });
		oreDimentionBlacklistProp.setComment("A blacklist of all the Dimensions ores shouldn't spawn in");
		oreDimentionBlacklistProp.setLanguageKey("config.ore_gen_blacklist.name");
		int[] oreDimentionBlacklist = oreDimentionBlacklistProp.getIntList();
		for (int anOreDimentionBlacklist : oreDimentionBlacklist) {
			this.oreDimentionsBlacklist.add(anOreDimentionBlacklist);
		}
		this.buildingsDimentionsBlacklist.clear();
		Property buildingsDimentionsBlacklistProp = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN,
				"structure_gen_blacklist", new int[] { -1 });
		buildingsDimentionsBlacklistProp.setComment("A blacklist of all the Dimensions structures shouldn't spawn in");
		buildingsDimentionsBlacklistProp.setLanguageKey("config.structure_gen_blacklist.name");
		int[] buildingsDimentionsBlacklist = buildingsDimentionsBlacklistProp.getIntList();
		for (int anbuildingsDimentionsBlacklist : buildingsDimentionsBlacklist) {
			this.buildingsDimentionsBlacklist.add(anbuildingsDimentionsBlacklist);
		}
	}
}
