
package matteroverdrive.fx;

import matteroverdrive.client.data.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class PhaserBoltRecoil extends Particle {
	private final float lavaParticleScale;

	public PhaserBoltRecoil(World world, double x, double y, double z, Color color, double dirX, double dirY,
			double dirZ) {
		super(world, x, y, z, dirX, dirY, dirZ);
		this.motionX += (this.rand.nextFloat() - 0.5f) * 0.2F;
		this.motionY += (this.rand.nextFloat() - 0.5f) * 0.2F;
		this.motionZ += (this.rand.nextFloat() - 0.5f) * 0.2F;
		this.particleRed = color.getFloatR();
		this.particleGreen = color.getFloatG();
		this.particleBlue = color.getFloatB();
		this.particleScale *= this.rand.nextFloat() * 0.5F + 1F;
		this.lavaParticleScale = this.particleScale;
		this.particleMaxAge = (int) (8d / (Math.random() * 0.8D + 0.2D));
		this.setParticleTextureIndex(rand.nextInt(2));
	}

	public PhaserBoltRecoil(World world, double x, double y, double z, Color color) {
		this(world, x, y, z, color, 0, 0, 0);
	}

	public int getBrightnessForRender(float f) {
		float f1 = ((float) this.particleAge + f) / (float) this.particleMaxAge;

		if (f1 < 0.0F) {
			f1 = 0.0F;
		}

		if (f1 > 1.0F) {
			f1 = 1.0F;
		}

		int i = super.getBrightnessForRender(f);
		short short1 = 240;
		int j = i >> 16 & 255;
		return short1 | j << 16;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float f) {
		return 1.0F;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f6 = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
		this.particleScale = this.lavaParticleScale * (1.0F - f6 * f6);
		super.renderParticle(buffer, entity, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		float f = (float) this.particleAge / (float) this.particleMaxAge;

		this.motionY -= 0.03D;
		try {
			this.move(this.motionX, this.motionY, this.motionZ);
		} catch (Exception e) {
			this.setExpired();
		}

		this.motionX *= 0.9990000128746033D;
		this.motionY *= 0.9990000128746033D;
		this.motionZ *= 0.9990000128746033D;
	}
}
