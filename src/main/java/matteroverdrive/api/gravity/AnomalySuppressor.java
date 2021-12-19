
package matteroverdrive.api.gravity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class AnomalySuppressor {
    private BlockPos pos;
    private int time;
    private float amount;

    public AnomalySuppressor(NBTTagCompound tagCompound) {
        readFromNBT(tagCompound);
    }

    public AnomalySuppressor(BlockPos pos, int time, float amount) {
        this.pos = pos;
        this.time = time;
        this.amount = amount;
    }

    public boolean update(AnomalySuppressor suppressor) {
        if (suppressor.pos.equals(pos)) {
            if (time < suppressor.time) {
                this.time = suppressor.time;
            }
            this.amount = suppressor.amount;
            return true;
        }
        return false;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setLong("block", pos.toLong());
        tagCompound.setByte("time", (byte) time);
        tagCompound.setFloat("amount", amount);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        pos = BlockPos.fromLong(tagCompound.getLong("block"));
        time = tagCompound.getByte("time");
        amount = tagCompound.getFloat("amount");
    }

    public void tick() {
        time--;
    }

    public boolean isValid() {
        return time > 0;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}
