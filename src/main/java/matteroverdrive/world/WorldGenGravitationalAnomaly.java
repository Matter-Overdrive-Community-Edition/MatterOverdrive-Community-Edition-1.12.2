
package matteroverdrive.world;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.data.world.WorldPosition2D;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.TileUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class WorldGenGravitationalAnomaly extends WorldGenerator implements IConfigSubscriber {
	public final Map<Integer, Integer> yLevelMap = new HashMap<>();
	private final HashSet<Integer> blacklist = new HashSet<>();
	private final HashSet<Integer> whitelist = new HashSet<>();
	private final float defaultChance;
	private float chance;
	private final int minMatter;
	private final int maxMatter;
	private final String name;

	public WorldGenGravitationalAnomaly(String name, float chance, int minMatter, int maxMatter) {
		this.defaultChance = chance;
		this.chance = chance;
		this.minMatter = minMatter;
		this.maxMatter = maxMatter;
		this.name = name;
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		if (isWorldValid(world) && isPosValid(world, pos) && random.nextFloat() < chance
				&& world.setBlockState(pos, MatterOverdrive.BLOCKS.gravitational_anomaly.getDefaultState())) {
			TileEntityGravitationalAnomaly anomaly = TileUtils.getNullableTileEntity(world, pos,
					TileEntityGravitationalAnomaly.class);
			if (anomaly == null) {
				anomaly = new TileEntityGravitationalAnomaly(minMatter + random.nextInt(maxMatter - minMatter));
				world.setTileEntity(pos, anomaly);
			} else {
				anomaly.markDirty();
			}
			GenPositionWorldData data = MOWorldGen.getWorldPositionData(world);
			data.addPosition(name, new WorldPosition2D(pos.getX(), pos.getZ()));
		}
		return false;
	}

	private boolean isPosValid(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}

	private boolean isWorldValid(World world) {
		if (!whitelist.isEmpty()) {
			return whitelist.contains(world.provider.getDimension())
					&& !blacklist.contains(world.provider.getDimension());
		}

		return !blacklist.contains(world.provider.getDimension());
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config) {
		chance = config.config.getFloat(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_SPAWN_CHANCE,
				String.format("%s.gravitational_anomaly", ConfigurationHandler.CATEGORY_WORLD_GEN), defaultChance, 0, 1,
				"Spawn Chance of Gravity Anomaly per chunk");
		yLevelMap.clear();
		String[] strings = config.config.getStringList(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_SPAWN_LEVEL,
				String.format("%s.gravitational_anomaly", ConfigurationHandler.CATEGORY_WORLD_GEN), new String[0],
				"Spawn Y level of the Gravity Anomaly");
		for (String s : strings) {
			String[] split = s.split(":");
			yLevelMap.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
		loadWhitelist(config);
		loadBlacklist(config);
	}

	private void loadWhitelist(ConfigurationHandler configurationHandler) {
		whitelist.clear();
		Property whitelistProp = configurationHandler.config.get(
				String.format("%s.gravitational_anomaly", ConfigurationHandler.CATEGORY_WORLD_GEN), "whitelist",
				new int[] { -1, 0 });
		whitelistProp.setComment("Gravitational Anomaly Dimension ID whitelist");
		int[] dimentions = whitelistProp.getIntList();
		for (int dimention : dimentions) {
			whitelist.add(dimention);
		}
	}

	private void loadBlacklist(ConfigurationHandler configurationHandler) {
		blacklist.clear();
		Property blacklistProp = configurationHandler.config.get(
				String.format("%s.gravitational_anomaly", ConfigurationHandler.CATEGORY_WORLD_GEN), "blacklist",
				new int[] {});
		blacklistProp.setComment("Gravitational Anomaly Dimension ID blacklist");
		int[] dimentions = blacklistProp.getIntList();
		for (int dimention : dimentions) {
			blacklist.add(dimention);
		}
	}
}
