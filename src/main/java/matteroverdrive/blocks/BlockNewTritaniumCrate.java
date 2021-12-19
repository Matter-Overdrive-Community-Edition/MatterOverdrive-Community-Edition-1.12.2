package matteroverdrive.blocks;

import matteroverdrive.api.wrench.IDismantleable;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.tile.TileEntityNewTritaniumCrate;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import matteroverdrive.util.MOBlockHelper;
import matteroverdrive.util.MOLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class BlockNewTritaniumCrate extends MOBlockMachine<TileEntityNewTritaniumCrate> implements IDismantleable {
    private static final AxisAlignedBB BOX_NORTH_SOUTH = new AxisAlignedBB(0, 0, 2 / 16d, 1, 12 / 16d, 14 / 16d);
    private static final AxisAlignedBB BOX_EAST_WEST = new AxisAlignedBB(2 / 16d, 0, 0, 14 / 16d, 12 / 16d, 1);

    public static final PropertyEnum<Color> COLOR = PropertyEnum.create("color", Color.class);

    public BlockNewTritaniumCrate(Material material, String name, int metadata) {
        super(material, name);

        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasRotation();
        setRotationType(MOBlockHelper.RotationType.FOUR_WAY);
    }

    public BlockNewTritaniumCrate(Material material, String name) {
        this(material, name, 0);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTY_DIRECTION, COLOR);
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Class<TileEntityNewTritaniumCrate> getTileEntityClass() {
        return TileEntityNewTritaniumCrate.class;
    }

    @Nullable
    @Override
    public TileEntityNewTritaniumCrate createNewTileEntity(World worldIn, int meta) {
        return new TileEntityNewTritaniumCrate();
    }

    /** Color Definitions **/
    public enum Color implements IStringSerializable {
        BASE(0, "base"),
        RED(1, "red"),
        GREEN(2, "green"),
        BROWN(3, "brown"),
        BLUE(4, "blue"),
        PURPLE(5, "purple"),
        CYAN(6, "cyan"),
        LIGHTGRAY(7, "light_gray"),
        GRAY(8, "gray"),
        PINK(9, "pink"),
        LIME(10, "lime"),
        YELLOW(11, "yellow"),
        LIGHTBLUE(12, "light_blue"),
        MAGENTA(13, "magenta"),
        ORANGE(14, "orange"),
        SECOND_WHITE(15, "second_white"),
        BLACK(16, "black"),
        WHITE(17, "white");


        private final int metadata;
        private final String name;

        Color(int metadata, String name) {
            this.metadata = metadata;
            this.name = name;
        }

        public int getMetadata() {
            return this.metadata;
        }

        @Nonnull
        @Override
        public String getName() {
            return this.name;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        ItemStack currentitem = playerIn.getHeldItem(EnumHand.MAIN_HAND);

        if (!currentitem.isEmpty()) {
            // Compare it against the base dye item.
            Item dye = new ItemStack(Items.DYE, 1).getItem();

            if (currentitem.getItem().equals(dye) || currentitem.getItem().equals(Items.WATER_BUCKET)) {
                int curDyeColor;

                if (currentitem.getItem().equals(Items.WATER_BUCKET)) {
                    curDyeColor = 0;
                } else {
                    curDyeColor = currentitem.getItemDamage();

                    // Convert bonemeal to white.
                    if (curDyeColor == 15) {
                        curDyeColor = 17;
                    }

                    // Convert ink sac to black.
                    if (curDyeColor == 0) {
                        curDyeColor = 16;
                    }
                }

                Color dyeColor = Color.values()[curDyeColor];

                playerIn.sendMessage(new TextComponentString("Using dye color: " + dyeColor));

                // Even though it seems redundant, both of these sections are needed. Client/Server perhaps?

                IBlockState bsc = worldIn.getBlockState(pos);

                worldIn.setBlockState(pos, bsc.withProperty(COLOR, dyeColor));

                if (worldIn.getTileEntity(pos) instanceof TileEntityNewTritaniumCrate) {
                    TileEntityNewTritaniumCrate tentc = (TileEntityNewTritaniumCrate) worldIn.getTileEntity(pos);

                    if (tentc != null) {
                        tentc.setColor(dyeColor.getMetadata());
                    }
                }

                if (currentitem.getItem().equals(dye)) {
                    if (! playerIn.capabilities.isCreativeMode) {
                        currentitem.setCount(currentitem.getCount() - 1);
                    }
                } else {
                    if (! playerIn.capabilities.isCreativeMode) {
                        // I'm worried about a memory leak here. What happens to the item that WAS in the main hand?
                        // Buckets are also not stackable, so no need to worry about having more than one in the main hand.
                        playerIn.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                    }
                }

                return true;
            }
        }

        TileEntity entity = worldIn.getTileEntity(pos);

        if (entity instanceof TileEntityNewTritaniumCrate) {
            //FMLNetworkHandler.openGui(entityPlayer, MatterOverdrive.instance, GuiHandler.TRITANIUM_CRATE, world, x, y, z);
            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), MatterOverdriveSounds.blocksCrateOpen, SoundCategory.BLOCKS, 0.5f, 1);

            playerIn.displayGUIChest(((TileEntityNewTritaniumCrate) entity).getInventory());

            return true;
        }

        return false;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, BlockPos pos) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops) {
        TileEntity tile = world.getTileEntity(pos);

        ItemStack s = new ItemStack(this);

        if (tile instanceof TileEntityTritaniumCrate) {
            IBlockState state = world.getBlockState(pos);

            state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);

            state.getBlock().removedByPlayer(state, world, pos, player, false);
        }

        return new ArrayList<>();
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityNewTritaniumCrate) {
            TileEntityNewTritaniumCrate tentc = (TileEntityNewTritaniumCrate) worldIn.getTileEntity(pos);

            if (tentc != null) {
                MOLog.info("Color being shown is: " + Color.values()[tentc.getColor()]);

                return super.getActualState(state, worldIn, pos).withProperty(COLOR, Color.values()[tentc.getColor()]);
            }
        }

        return super.getActualState(state, worldIn, pos);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
