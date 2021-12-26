
package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.pattern_storage.TileEntityMachinePatternStorage;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockPatternStorage extends MOBlockMachine<TileEntityMachinePatternStorage> {
    public boolean hasVentParticles;

    public BlockPatternStorage(Material material, String name) {
        super(material, name);
		setHasRotation();
        setHardness(20.0F);
        setLightOpacity(5);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        if(side == getOppositeSide(metadata))
        {
            return MatterOverdriveIcons.Vent;
        }

        return MatterOverdriveIcons.Base;
    }*/

    @Override
    public Class<TileEntityMachinePatternStorage> getTileEntityClass() {
        return TileEntityMachinePatternStorage.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachinePatternStorage();
    }

    /*@Override
	public int getRenderType()
    {
        return RendererBlockPatternStorage.renderID;
    }*/

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public void onConfigChanged(ConfigurationHandler config) {
        super.onConfigChanged(config);
        hasVentParticles = config.getMachineBool(getTranslationKey(), "particles.vent", true, "Should vent particles be displayed");
        TileEntityMachinePatternStorage.ENERGY_CAPACITY = config.getMachineInt(getTranslationKey(), "storage.energy", 64000, String.format("How much energy can the %s hold", getLocalizedName()));
        TileEntityMachinePatternStorage.ENERGY_TRANSFER = config.getMachineInt(getTranslationKey(), "transfer.energy", 128, String.format("The Transfer speed of the %s", getLocalizedName()));
    }
}
