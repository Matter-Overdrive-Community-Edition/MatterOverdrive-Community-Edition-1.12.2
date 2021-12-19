
package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.tile.TileEntityMachineNetworkSwitch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static matteroverdrive.util.MOBlockHelper.RotationType;

public class BlockNetworkSwitch extends MOBlockMachine<TileEntityMachineNetworkSwitch> {
    public BlockNetworkSwitch(Material material, String name) {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setRotationType(RotationType.PREVENT);
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registrar)
    {
        activeIcon = registrar.registerIcon(this.getTextureName() + "_active");
        this.blockIcon = registrar.registerIcon(this.getTextureName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int meta)
    {
        TileEntity entity = world.getTileEntity(x,y,z);
        if (entity instanceof TileEntityMachineNetworkSwitch)
        {
            if (((TileEntityMachineNetworkSwitch) entity).isActive())
            {
                return activeIcon;
            }
        }
        return blockIcon;
    }*/

    @Override
    public Class<TileEntityMachineNetworkSwitch> getTileEntityClass() {
        return TileEntityMachineNetworkSwitch.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
        return new TileEntityMachineNetworkSwitch();
    }
}
