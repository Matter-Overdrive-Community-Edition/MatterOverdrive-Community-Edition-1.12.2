
package matteroverdrive.blocks;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.tile.TileEntityMachineMatterRecycler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockMatterRecycler extends MOMatterEnergyStorageBlock<TileEntityMachineMatterRecycler> {
    public static final PropertyBool RUNNING = PropertyBool.create("running");

    public BlockMatterRecycler(Material material, String name) {
        super(material, name, true, true);
        setHasRotation();
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        this.setDefaultState(getBlockState().getBaseState().withProperty(RUNNING, false).withProperty(PROPERTY_DIRECTION, EnumFacing.NORTH));
        setHasGui(true);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTY_DIRECTION, RUNNING);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        IBlockState blockState = worldIn.getBlockState(pos);

        worldIn.setBlockState(pos, blockState.withProperty(RUNNING, false));
    }

    public static void setState(boolean running, World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (running) {
            worldIn.setBlockState(
                    pos,
                    MatterOverdrive.BLOCKS.recycler.getDefaultState()
                        .withProperty(PROPERTY_DIRECTION, state.getValue(PROPERTY_DIRECTION))
                        .withProperty(RUNNING, true),
                    3
            );
        } else {
            worldIn.setBlockState(
                    pos,
                    MatterOverdrive.BLOCKS.recycler.getDefaultState()
                        .withProperty(PROPERTY_DIRECTION, state.getValue(PROPERTY_DIRECTION))
                        .withProperty(RUNNING, false),
                    3
            );
        }

        if (tileEntity != null) {
            tileEntity.validate();

            worldIn.setTileEntity(pos, tileEntity);
        }
    }

//    @SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister iconRegister)
//    {
//        this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":decomposer_top");
//        this.iconSideAnim = iconRegister.registerIcon(Reference.MOD_ID + ":recycler_side_anim");
//    }
//
//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(int side, int meta)
//    {
//        if (side == MOBlockHelper.getAboveSide(meta))
//        {
//            return iconTop;
//        }
//
//        return MatterOverdriveIcons.Recycler;
//    }
//
//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
//    {
//        if (side != MOBlockHelper.getAboveSide(blockAccess.getBlockMetadata(x, y, z)))
//        {
//            if (blockAccess.getTileEntity(x, y, z) instanceof TileEntityMachineMatterRecycler)
//            {
//                if (((TileEntityMachineMatterRecycler) blockAccess.getTileEntity(x, y, z)).isActive())
//                {
//                    return iconSideAnim;
//                }
//            }
//        }
//        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
//    }

    @Override
    public Class<TileEntityMachineMatterRecycler> getTileEntityClass() {
        return TileEntityMachineMatterRecycler.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
        return new TileEntityMachineMatterRecycler();
    }

/*
	@Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }*/
}
