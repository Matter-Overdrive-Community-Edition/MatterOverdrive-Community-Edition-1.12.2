
package matteroverdrive.items;

import matteroverdrive.items.includes.EnergyContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class CreativeBattery extends Battery {
    public CreativeBattery(String name) {
        super(name, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public ICapabilitySerializable<NBTTagCompound> createProvider(ItemStack stack) {
        return new InfiniteProvider(getCapacity(), getInput(), getOutput());
    }

    @Override
    protected boolean addPoweredItem() {
        return false;
    }

    public static class InfiniteProvider implements ICapabilitySerializable<NBTTagCompound> {

        private EnergyContainer container;

        public InfiniteProvider(int capacity, int input, int output) {
            container = new EnergyContainer(capacity, input, output, 0) {
                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return 0;
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {
                    return Math.max(maxExtract, this.maxExtract);
                }

                @Override
                public int getEnergyStored() {
                    return Integer.MAX_VALUE;
                }
            };
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityEnergy.ENERGY) {
                return CapabilityEnergy.ENERGY.cast(container);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return container.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound tag) {
            container.deserializeNBT(tag);
        }
    }
}