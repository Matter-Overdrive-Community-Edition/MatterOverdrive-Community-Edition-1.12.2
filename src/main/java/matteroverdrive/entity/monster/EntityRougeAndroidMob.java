
package matteroverdrive.entity.monster;

import io.netty.buffer.ByteBuf;
import matteroverdrive.Reference;
import matteroverdrive.api.entity.IPathableMob;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.tile.TileEntityAndroidSpawner;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityRougeAndroidMob extends EntityMob
		implements IEntityAdditionalSpawnData, IPathableMob<EntityRougeAndroidMob> {
	private static final ResourceLocation androidNames = new ResourceLocation(
			Reference.PATH_INFO + "android_names.txt");
	private static final String[] names = MOStringHelper.readTextFile(androidNames).split(",");
	private BlockPos homePosition = BlockPos.ORIGIN;
	boolean fromSpawner;
	private BlockPos spawnerPosition;
	private int currentPathIndex;
	private Vec3d[] path;
	private int maxPathTargetRangeSq;
	private int visorColor;
	private ScorePlayerTeam team;
	private boolean legendary;
	private int level;

	public EntityRougeAndroidMob(World world) {
		super(world);
		if (!world.isRemote) {
			setAndroidLevel((int) (MathHelper
					.clamp(Math.abs(rand.nextGaussian() * (1 + world.getDifficulty().getId() * 0.25)), 0, 3)));
			boolean isLegendary = rand.nextDouble() < EntityRogueAndroid.LEGENDARY_SPAWN_CHANCE * getAndroidLevel();
			setLegendary(isLegendary);
			init();
			// getNavigator().setAvoidsWater(true);
			// getNavigator().setCanSwim(false);
		}
	}

	public EntityRougeAndroidMob(World world, int level, boolean legendary) {
		super(world);
		setAndroidLevel(level);
		setLegendary(legendary);
		init();
	}

	private void init() {
		String name = getIsLegendary()
				? String.format("%s %s ", Reference.UNICODE_LEGENDARY,
						MOStringHelper.translateToLocal("rarity.legendary"))
				: "";
		name += String.format("[%s] ", getAndroidLevel());
		name += names[rand.nextInt(names.length)];
		setCustomNameTag(name);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
				.setBaseValue(getIsLegendary() ? 128 : getAndroidLevel() * 10 + 32);
		this.setHealth(this.getMaxHealth());

		if (fromSpawner) {
			if (!addToSpawner(spawnerPosition)) {
				setDead();
			}
		}

		if (getIsLegendary()) {
			setVisorColor(Reference.COLOR_HOLO_RED.getColor());
		} else {
			switch (getAndroidLevel()) {
			case 0:
				setVisorColor(Reference.COLOR_HOLO.getColor());
				break;
			case 1:
				setVisorColor(Reference.COLOR_HOLO_YELLOW.getColor());
				break;
			case 2:
				setVisorColor(Reference.COLOR_HOLO_PURPLE.getColor());
				break;
			default:
				setVisorColor(0xFFFFFFFF);
			}
		}
	}

	public TextFormatting getNameColor() {
		if (getIsLegendary()) {
			return TextFormatting.GOLD;
		} else {
			switch (getAndroidLevel()) {
			case 0:
				return TextFormatting.GRAY;
			case 1:
				return TextFormatting.DARK_AQUA;
			case 2:
				return TextFormatting.DARK_PURPLE;
			default:
				return null;
			}

		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
		setLegendary(nbtTagCompound.getBoolean("Legendary"));
		setAndroidLevel(nbtTagCompound.getByte("Level"));
		setVisorColor(nbtTagCompound.getInteger("VisorColor"));
		if (nbtTagCompound.hasKey("Team", Constants.NBT.TAG_STRING)) {
			ScorePlayerTeam team = world.getScoreboard().getTeam(nbtTagCompound.getString("Team"));
			if (team != null) {
				setTeam(team);
			} else {
				setDead();
			}
		}
		if (nbtTagCompound.hasKey("SpawnerPos", Constants.NBT.TAG_COMPOUND)) {
			spawnerPosition = BlockPos.fromLong(nbtTagCompound.getLong("SpawnerPos"));
			this.fromSpawner = true;
		}
		currentPathIndex = nbtTagCompound.getInteger("CurrentPathIndex");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
		nbtTagCompound.setByte("Level", (byte) getAndroidLevel());
		nbtTagCompound.setBoolean("Legendary", getIsLegendary());
		nbtTagCompound.setInteger("VisorColor", getVisorColor());
		if (getTeam() != null) {
			nbtTagCompound.setString("Team", getTeam().getName());
		}
		if (spawnerPosition != null) {
			nbtTagCompound.setLong("SpawnerPos", spawnerPosition.toLong());
		}
		nbtTagCompound.setInteger("CurrentPathIndex", currentPathIndex);
	}

	private boolean addToSpawner(BlockPos position) {
		this.spawnerPosition = position;
		TileEntity spawnerEntity = world.getTileEntity(position);
		if (spawnerEntity instanceof TileEntityAndroidSpawner) {
			((TileEntityAndroidSpawner) spawnerEntity).addSpawnedAndroid(this);
			return true;
		}
		return false;
	}

	public void setAttackTarget(EntityLivingBase target) {
		if (target != null && target.getTeam() != null) {
			if (!target.getTeam().isSameTeam(getTeam())) {
				super.setAttackTarget(target);
			}
		} else {
			super.setAttackTarget(target);
		}
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potion) {
		return false;
	}

	@Override
	public boolean getCanSpawnHere() {
		return getCanSpawnHere(false, false, false) && !hasToManyAndroids()
				&& rand.nextFloat() < EntityRogueAndroid.SPAWN_CHANCE;
	}

	public boolean hasToManyAndroids() {
		Chunk chunk = world.getChunk(chunkCoordX, chunkCoordZ);
		int androidCount = 0;
		for (int i = 0; i < chunk.getEntityLists().length; i++) {
			for (Entity entity : chunk.getEntityLists()[i]) {
				if (entity instanceof EntityRougeAndroidMob) {
					androidCount++;
					if (androidCount > EntityRogueAndroid.MAX_ANDROIDS_PER_CHUNK) {
						return true;
					}
				}
			}

		}
		return false;
	}

	public boolean getCanSpawnHere(boolean ignoreEntityCollision, boolean ignoreLight, boolean ignoreDimension) {
		if (!ignoreDimension) {
			if (!EntityRogueAndroid.dimensionWhitelist.isEmpty()) {
				return EntityRogueAndroid.dimensionWhitelist.contains(world.provider.getDimension())
						&& inDimensionBlacklist();
			}
			if (inDimensionBlacklist()) {
				return false;
			}
		}
		boolean light = ignoreLight || isValidLightLevel();
		boolean entityCollison = ignoreEntityCollision
				|| this.world.checkNoEntityCollision(this.getEntityBoundingBox());
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && light && entityCollison
				&& this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()
				&& !this.world.containsAnyLiquid(this.getEntityBoundingBox());
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		float weight = 1 - this.world.getLightBrightness(pos);
		weight *= this.world.isSideSolid(pos, EnumFacing.UP) ? 0 : 1;
		weight /= (float) Math.abs(pos.getY() - posY);
		return weight;
	}

	protected void addRandomArmor() {
		if (this.rand.nextFloat() < 0.15F) {
			int i = this.rand.nextInt(2);
			float f = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.25F;

			if (this.rand.nextFloat() < 0.095F) {
				++i;
			}

			if (this.rand.nextFloat() < 0.095F) {
				++i;
			}

			if (this.rand.nextFloat() < 0.095F) {
				++i;
			}

			// TODO: Do the random armor thing
			replaceItemInInventory(100, ItemStack.EMPTY); // FEET
			replaceItemInInventory(101, ItemStack.EMPTY); // LEGS
			replaceItemInInventory(102, ItemStack.EMPTY); // CHEST
			replaceItemInInventory(103, ItemStack.EMPTY); // HEAD
		}
	}

	@Override
	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		return this.getHomePosition().distanceSq(pos) < (double) (maxPathTargetRangeSq * maxPathTargetRangeSq);
	}

	@Override
	public boolean hasHome() {
		return getCurrentTarget() != null;
	}

	@Override
	public BlockPos getHomePosition() {
		Vec3d currentTarget = getCurrentTarget();
		if (currentTarget == null) {
			return this.homePosition;
		} else {
			return new BlockPos((int) currentTarget.x, (int) currentTarget.y, (int) currentTarget.z);
		}
	}

	private boolean inDimensionBlacklist() {
		return EntityRogueAndroid.dimensionBlacklist.contains(world.provider.getDimension());
	}

	public int getAndroidLevel() {
		return this.level;
	}

	public void setAndroidLevel(int level) {
		this.level = level;
	}

	public void setLegendary(boolean legendary) {
		this.legendary = legendary;
		if (legendary) {
			this.height = 1.8f * 1.6f;
		} else {
			this.height = 1.8f;
		}
	}

	public boolean getIsLegendary() {
		return legendary;
	}

	@Override
	public String getCustomNameTag() {
		if (hasTeam()) {
			return getTeam().formatString(super.getCustomNameTag());
		} else {
			TextFormatting color = getNameColor();
			if (color != null) {
				return color + super.getCustomNameTag();
			}
		}
		return super.getCustomNameTag();
	}

	@Override
	public ScorePlayerTeam getTeam() {
		return team;
	}

	public void setTeam(ScorePlayerTeam team) {
		this.team = team;
	}

	public boolean hasTeam() {
		return getTeam() != null;
	}

	public boolean wasSpawnedFrom(TileEntityAndroidSpawner spawner) {
		if (spawnerPosition != null) {
			TileEntity tileEntity = world.getTileEntity(spawnerPosition);
			return tileEntity == spawner;
		}
		return false;
	}

	public void setSpawnerPosition(BlockPos position) {
		this.spawnerPosition = position;
		this.fromSpawner = true;
	}

	@Override
	public void setDead() {
		if (spawnerPosition != null) {
			TileEntity spawner = world.getTileEntity(spawnerPosition);
			if (spawner instanceof TileEntityAndroidSpawner) {
				((TileEntityAndroidSpawner) spawner).removeAndroid(this);
			}
		}
		this.isDead = true;
	}

	public int getVisorColor() {
		return visorColor;
	}

	public void setVisorColor(int color) {
		visorColor = color;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeByte(level);
		buffer.writeBoolean(legendary);
		buffer.writeInt(visorColor);
		buffer.writeBoolean(hasTeam());
		if (hasTeam()) {
			ByteBufUtils.writeUTF8String(buffer, getTeam().getName());
		}
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		setAndroidLevel(additionalData.readByte());
		setLegendary(additionalData.readBoolean());
		setVisorColor(additionalData.readInt());
		if (additionalData.readBoolean()) {
			String teamName = ByteBufUtils.readUTF8String(additionalData);
			ScorePlayerTeam team = world.getScoreboard().getTeam(teamName);
			if (team != null) {
				setTeam(team);
			}
		}
	}

	@Override
	public Vec3d getCurrentTarget() {
		if (path != null && currentPathIndex < path.length) {
			return path[currentPathIndex];
		}
		return null;
	}

	@Override
	public void onTargetReached(Vec3d pos) {
		if (currentPathIndex < path.length - 1) {
			currentPathIndex++;
		}
	}

	@Override
	public boolean isNearTarget(Vec3d pos) {
		return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
	}

	@Override
	public EntityRougeAndroidMob getEntity() {
		return this;
	}

	public void setPath(Vec3d[] path, int range) {
		this.path = path;
		maxPathTargetRangeSq = range * range;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return MatterOverdriveSounds.mobsRogueAndroidSay;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MatterOverdriveSounds.mobsRogueAndroidDeath;
	}

	@Override
	protected float getSoundVolume() {
		return 0.5f;
	}

	public int getTalkInterval() {
		return 20 * 24;
	}
}
