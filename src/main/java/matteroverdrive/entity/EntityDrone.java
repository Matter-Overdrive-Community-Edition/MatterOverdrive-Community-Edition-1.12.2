
package matteroverdrive.entity;

import java.util.UUID;

import com.google.common.base.Optional;

import matteroverdrive.entity.ai.EntityAIFollowCreator;
import matteroverdrive.entity.ai.PathNavigateFly;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityDrone extends EntityCreature implements IEntityOwnable {
	protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager
			.createKey(EntityDrone.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private BlockPos targetPos;
	private EntityLivingBase owner;

	public EntityDrone(World worldIn) {
		super(worldIn);
		this.setSize(8f / 16f, 8f / 16f);
		this.moveHelper = new DroneMoveHelper(this);

		this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
		this.tasks.addTask(3, new EntityAIFollowCreator<>(this, 0.2f, 5f, 3.0F));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
	}

	@Override
	public PathNavigate getNavigator() {
		return this.navigator;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (getOwnerId() != null) {
			if (!world.isRemote) {
				setOwnerId(player.getUniqueID());
				this.world.setEntityState(this, (byte) 7);
			} else {
				this.world.setEntityState(this, (byte) 6);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (targetPos != null) {
			 this.motionY = MathHelper.clamp(((targetPos.getY() + 2) - this.posY), -0.2f, 0.2f);
		}

		if (!getNavigator().noPath()) {
			Vec3d target = getNavigator().getPath().getPosition(this);
		}
		if (world.isRemote) {
			if (ticksExisted % 2 == 0) {
				int motionSpeed = Math.min(6,
						(int) (Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ) * 30d));
				Vec3d look = getLook(1);
				for (int i = 0; i < motionSpeed; i++) {
					world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, posX - look.x * 0.25,
							posY + getEyeHeight() + 0.2 - look.y * 0.2, posZ - look.z * 0.2, -motionX * 2, -motionY * 2,
							-motionZ * 2);
				}
			}
			
			  //IBlockState bottomBlock = world.getBlockState(targetPos.offset(EnumFacing.DOWN)); 
			  //if (bottomBlock.getBlock() != Blocks.AIR) { 
			//	  int blockID = Block.getStateId(bottomBlock); for (int i = 0;i < 3;i++) { 
				//	  double angle = rand.nextFloat()*Math.PI*2; float innerRadius = 0.4f; double x = posX +
			  //(float)Math.sin(angle) * innerRadius; double z = posZ +  (float)Math.cos(angle) * innerRadius; float dirX = -(float) Math.sin(angle +
			  //Math.PI) * 0.2f; float dirZ = -(float) Math.cos(angle + Math.PI) * 0.2f;
			  //world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x, targetPos.getY(), z, dirX,
			  //0.15, dirZ, blockID); } }
			 
		}
		 this.motionY *= 0.6000000238418579D;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		tagCompound.setLong("target", targetPos.toLong());
		if (this.getOwnerId() == null) {
			tagCompound.setString("OwnerUUID", "");
		} else {
			tagCompound.setString("OwnerUUID", this.getOwnerId().toString());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) {
		super.readEntityFromNBT(tagCompound);
		if (tagCompound.hasKey("target")) {
			targetPos = BlockPos.fromLong(tagCompound.getLong("target"));
		}
		String s = "";

		if (tagCompound.hasKey("OwnerUUID", 8)) {
			s = tagCompound.getString("OwnerUUID");
		} else {
			String s1 = tagCompound.getString("Owner");
			s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
		}

		if (!s.isEmpty()) {
			this.setOwnerId(UUID.fromString(s));
		}
	}

	@Override
	public void fall(float distance, float damageMultiplier) {

	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new PathNavigateFly(this, worldIn);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {

	}

	
	  public void moveEntityWithHeading(float strafe, float forward) { if
	  (this.isInWater()) { this.moveRelative(strafe, forward, 0.02F, forward);
	  this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ); this.motionX *=
	  0.800000011920929D; this.motionY *= 0.800000011920929D; this.motionZ *=
	  0.800000011920929D; } else if (this.isInLava()) { this.moveRelative(strafe,
	  forward, 0.02F, forward); this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
	  this.motionX *= 0.5D; this.motionY *= 0.5D; this.motionZ *= 0.5D; } else {
	  float f = 0.91F;
	  
	  if (this.onGround) { f = this.world.getBlockState(new
	  BlockPos(MathHelper.floor(this.posX),
	  MathHelper.floor(this.getEntityBoundingBox().minY) - 1,
	  MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F; }
	  
	  float f1 = 0.16277136F / (f * f * f); this.moveRelative(strafe, forward,
	  this.onGround ? 0.1F * f1 : 0.02F, f1); f = 0.91F;
	  
	  if (this.onGround) { f = this.world.getBlockState(new
	  BlockPos(MathHelper.floor(this.posX),
	  MathHelper.floor(this.getEntityBoundingBox().minY) - 1,
	  MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F; }
	  
	  this.move(MoverType.SELF,this.motionX, this.motionY, this.motionZ);
	  this.motionX *= (double)f; this.motionY *= (double)f; this.motionZ *=
	  (double)f; }
	  
	  this.prevLimbSwingAmount = this.limbSwingAmount; double d1 = this.posX -
	  this.prevPosX; double d0 = this.posZ - this.prevPosZ; float f2 =
	  MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
	  
	  if (f2 > 1.0F) { f2 = 1.0F; }
	  
	  this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F; this.limbSwing +=
	  this.limbSwingAmount; }
	 
	@Override
	public boolean isOnLadder() {
		return false;
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	@Override
	public float getEyeHeight() {
		return this.height / 2.0F;
	}

	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		targetPos = getPosition();
		return livingdata;
	}

	@Override
	public UUID getOwnerId() {
		Optional<UUID> uuid = this.dataManager.get(OWNER_UNIQUE_ID);
		return uuid.isPresent() ? uuid.get() : null;
	}

	public void setOwnerId(UUID ownerUuid) {
		this.dataManager.set(OWNER_UNIQUE_ID, Optional.of(ownerUuid));
	}

	public EntityLivingBase getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
		} catch (IllegalArgumentException var2) {
			return null;
		}
	}

	public boolean isOwner(EntityLivingBase entityIn) {
		return entityIn == this.getOwner();
	}

	public Team getTeam() {
		EntityLivingBase entitylivingbase = this.getOwner();

		if (entitylivingbase != null) {
			return entitylivingbase.getTeam();
		}

		return super.getTeam();
	}

	public boolean isOnSameTeam(EntityLivingBase otherEntity) {
		EntityLivingBase entitylivingbase = this.getOwner();

		if (otherEntity == entitylivingbase) {
			return true;
		}

		if (entitylivingbase != null) {
			return entitylivingbase.isOnSameTeam(otherEntity);
		}

		return super.isOnSameTeam(otherEntity);
	}

	static class DroneMoveHelper extends EntityMoveHelper {
		private EntityDrone parentEntity;
		private int courseChangeCooldown;

		public DroneMoveHelper(EntityDrone entityDrone) {
			super(entityDrone);
			this.parentEntity = entityDrone;
		}

		public void onUpdateMoveHelper() {
			if (this.action == Action.MOVE_TO) {
				int i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D);
				double d0 = this.posX - this.parentEntity.posX;
				double d1 = this.posY - (double) i;
				double d2 = this.posZ - this.parentEntity.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				float f = MathHelper.sqrt(posX * posX + posY * posY);
				this.parentEntity.prevRotationYaw = this.parentEntity.rotationYaw = (float) (MathHelper.atan2(posX,
						posZ) * 180.0D / Math.PI);
				this.parentEntity.prevRotationPitch = this.parentEntity.rotationPitch = (float) (MathHelper.atan2(posY,
						(double) f) * 180.0D / Math.PI);

				d3 = (double) MathHelper.sqrt(d3);

				this.parentEntity.motionX = d0 / d3 * this.speed;
				this.parentEntity.motionY = d1 / d3 * this.speed;
				this.parentEntity.motionZ = d2 / d3 * this.speed;
				this.action = Action.WAIT;
			}
		}
	}
}