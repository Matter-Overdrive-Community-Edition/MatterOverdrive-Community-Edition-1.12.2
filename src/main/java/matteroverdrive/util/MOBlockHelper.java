
package matteroverdrive.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class MOBlockHelper {
	public static final int[][] SIDE__MOD = new int[][] { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 },
			{ -1, 0, 0 }, { 1, 0, 0 } };
	public static final byte[] SIDE_LEFT = new byte[] { (byte) 4, (byte) 5, (byte) 5, (byte) 4, (byte) 2, (byte) 3 };
	public static final byte[] SIDE_RIGHT = new byte[] { (byte) 5, (byte) 4, (byte) 4, (byte) 5, (byte) 3, (byte) 2 };
	public static final byte[] SIDE_ABOVE = new byte[] { (byte) 3, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 1 };
	public static final byte[] SIDE_BELOW = new byte[] { (byte) 2, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0 };

	public static EnumFacing getLeftSide(EnumFacing side) {
		return EnumFacing.VALUES[SIDE_LEFT[side.ordinal()]];
	}

	public static EnumFacing getRightSide(EnumFacing side) {
		return EnumFacing.VALUES[SIDE_RIGHT[side.ordinal()]];
	}

	public static EnumFacing getAboveSide(EnumFacing side) {
		return EnumFacing.VALUES[SIDE_ABOVE[side.ordinal()]];
	}

	public static int getBelowSide(EnumFacing side) {
		return SIDE_BELOW[side.ordinal()];
	}

	public static EnumFacing determineXZPlaceFacing(EntityLivingBase placer) {
		int rotation = MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch (rotation) {
		case 0:
			return EnumFacing.UP;
		case 1:
			return EnumFacing.EAST;
		case 2:
			return EnumFacing.SOUTH;
		case 3:
			return EnumFacing.WEST;
		default:
			return EnumFacing.SOUTH;
		}
	}

	public enum RotationType {
		PREVENT, FOUR_WAY, SIX_WAY;
	}
}
