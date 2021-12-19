
package matteroverdrive.items.includes;

import matteroverdrive.api.EmptyEnergyStorage;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class MOItemEnergyContainer extends MOBaseItem {

    public MOItemEnergyContainer(String name) {
        super(name);
        setMaxStackSize(1);
    }

    public static IEnergyStorage getStorage(ItemStack stack) {
        if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
            return stack.getCapability(CapabilityEnergy.ENERGY, null);
        return EmptyEnergyStorage.INSTANCE;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return getStorage(stack).getMaxEnergyStored();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        IEnergyStorage storage = getStorage(stack);
        return (storage.getMaxEnergyStored() - storage.getEnergyStored()) / (double) storage.getMaxEnergyStored();
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 15866137;
    }

    @Override
    public void addDetails(ItemStack stack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        IEnergyStorage storage = getStorage(stack);
        infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(storage.getEnergyStored(), storage.getMaxEnergyStored()));
    }

    @Override
    public boolean hasDetails(ItemStack itemStack) {
        return true;
    }

    public ICapabilityProvider createProvider(ItemStack stack) {
        return new EnergyProvider(getCapacity(), getInput(), getOutput()).setStack(stack);
    }

    protected abstract int getCapacity();

    protected abstract int getInput();

    protected abstract int getOutput();

    protected boolean addPoweredItem() {
        return true;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            if (addPoweredItem()) {
                ItemStack powered = new ItemStack(this);
                IEnergyStorage storage = getStorage(powered);
                if (storage instanceof EnergyContainer)
                    ((EnergyContainer) storage).setFull();
                items.add(powered);
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return createProvider(stack);
    }

    public static class EnergyProvider implements ICapabilityProvider {
        private EnergyContainer container;

        public EnergyProvider(int capacity, int input, int output) {
            container = new EnergyContainer(capacity, input, output);
        }

        public EnergyProvider(int capacity, int through) {
            this(capacity, through, through);
        }

        public EnergyProvider setStack(ItemStack stack) {
            container.setItemStack(stack);
            return this;
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityEnergy.ENERGY;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityEnergy.ENERGY) {
                return (T) container;
            }
            return null;
        }
    }
}