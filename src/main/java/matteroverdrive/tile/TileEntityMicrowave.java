package matteroverdrive.tile;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.FoodFurnaceSlot;
import matteroverdrive.data.inventory.RemoveOnlySlot;
import matteroverdrive.data.recipes.MicrowaveRecipe;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class TileEntityMicrowave extends MOTileEntityMachineEnergy {
    private static final EnumSet<UpgradeTypes> upgradeTypes = EnumSet.of(UpgradeTypes.PowerUsage, UpgradeTypes.Speed,
            UpgradeTypes.PowerStorage, UpgradeTypes.PowerTransfer, UpgradeTypes.Muffler);

    public int INPUT_SLOT_ID, OUTPUT_SLOT_ID;

    @SideOnly(Side.CLIENT)
    private float nextHeadX, nextHeadY;
    @SideOnly(Side.CLIENT)
    private float lastHeadX, lastHeadY;
    @SideOnly(Side.CLIENT)

    private float headAnimationTime;
    private int cookTime;
    private MicrowaveRecipe cachedRecipe;

    public TileEntityMicrowave() {
        super(4);
        energyStorage.setCapacity(512000);
        energyStorage.setMaxExtract(256);
        energyStorage.setMaxReceive(256);

        playerSlotsHotbar = true;
        playerSlotsMain = true;
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        INPUT_SLOT_ID = inventory.AddSlot(new FoodFurnaceSlot(true).setSendToClient(true));
        OUTPUT_SLOT_ID = inventory.AddSlot(new RemoveOnlySlot(false).setSendToClient(true));

        super.RegisterSlots(inventory);
    }

    public boolean canPutInOutput() {
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT_ID);

        return outputStack.isEmpty();

//        return outputStack.isEmpty() || (cachedRecipe != null && outputStack.isItemEqual(cachedRecipe.getOutput(this)));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        super.writeCustomNBT(nbt, categories, toDisk);

        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setInteger("cookTime", cookTime);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        super.readCustomNBT(nbt, categories);

        if (categories.contains(MachineNBTCategory.DATA)) {
            cookTime = nbt.getInteger("cookTime");
        }
    }

    @Override
    public boolean getServerActive() {
        return isCooking() && this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick();
    }

    public int getEnergyDrainPerTick() {
        int maxEnergy = getEnergyDrainMax();
        int speed = getSpeed();
        if (speed > 0) {
            return maxEnergy / speed;
        }
        return 0;
    }

    public int getEnergyDrainMax() {
        if (cachedRecipe != null) {
            return (int) (cachedRecipe.getEnergy() * getUpgradeMultiply(UpgradeTypes.PowerUsage));
        }

        return 0;
    }

    public int getSpeed() {
        if (cachedRecipe != null) {
            return (int) (cachedRecipe.getTime() * getUpgradeMultiply(UpgradeTypes.Speed));
        }

        return 0;
    }

//    public boolean isCooking() {
//        return cachedRecipe != null &&canPutInOutput();
//    }

    public boolean isCooking() {
        return canPutInOutput();
    }

    @Override
    public SoundEvent getSound() {
        return MatterOverdriveSounds.machine;
    }

    @Override
    public boolean hasSound() {
        return true;
    }

    @Override
    public float soundVolume() {
        ItemStack stack = this.getStackInSlot(INPUT_SLOT_ID);

        if (getUpgradeMultiply(UpgradeTypes.Muffler) == 2d || stack.isEmpty()) {
            return 0.0f;
        }

        return 1;
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote && isActive()) {
            handleHeadAnimation();
        }
        manageCooking();
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
        return slot == OUTPUT_SLOT_ID;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return upgradeTypes.contains(type);
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {
        if (event instanceof MachineEvent.Awake) {
//            calculateRecipe();
        }
    }

    @Override
    public float getProgress() {
        float speed = (float) getSpeed();
        if (speed > 0) {
            return (float) (cookTime) / speed;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    protected void handleHeadAnimation() {
        if (headAnimationTime >= 1) {
            lastHeadX = nextHeadX;
            lastHeadY = nextHeadY;
            nextHeadX = MathHelper.clamp((float) random.nextGaussian(), -1, 1);
            nextHeadY = MathHelper.clamp((float) random.nextGaussian(), -1, 1);
            headAnimationTime = 0;
        }

        headAnimationTime += 0.05f;
    }

    @SideOnly(Side.CLIENT)
    public float geatHeadX() {
        return MOMathHelper.Lerp(lastHeadX, nextHeadX, headAnimationTime);
    }

    @SideOnly(Side.CLIENT)
    public float geatHeadY() {
        return MOMathHelper.Lerp(lastHeadY, nextHeadY, headAnimationTime);
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        return super.decrStackSize(slot, size);
    }

    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        super.setInventorySlotContents(slot, itemStack);
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        return new int[] { INPUT_SLOT_ID, OUTPUT_SLOT_ID };
    }

    protected void manageCooking() {
        if (!world.isRemote) {
            if (this.isCooking()) {
                if (this.energyStorage.getEnergyStored() >= getEnergyDrainPerTick()) {

                    this.cookTime++;

                    energyStorage.modifyEnergyStored(-getEnergyDrainPerTick());

                    UpdateClientPower();

                    if (this.cookTime >= getSpeed()) {
                        this.cookTime = 0;

                        this.cookItem();
                    }
                }
            }
        }

        if (!this.isCooking()) {
            this.cookTime = 0;
        }
    }

    public void cookItem() {
        if (cachedRecipe != null && canPutInOutput()) {
            ItemStack outputSlot = inventory.getStackInSlot(OUTPUT_SLOT_ID);

            if (!outputSlot.isEmpty()) {
                outputSlot.grow(1);
            } else {
                inventory.setInventorySlotContents(OUTPUT_SLOT_ID, cachedRecipe.getOutput(this));
            }

            inventory.decrStackSize(INPUT_SLOT_ID, 1);
        }
    }
}