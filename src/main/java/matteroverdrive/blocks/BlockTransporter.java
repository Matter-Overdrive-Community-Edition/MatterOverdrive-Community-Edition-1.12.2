
package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockTransporter extends MOMatterEnergyStorageBlock<TileEntityMachineTransporter> {

	public BlockTransporter(Material material, String name) {
		super(material, name, true, true);
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setHasGui(true);
	}

	@Override
	public Class<TileEntityMachineTransporter> getTileEntityClass() {
		return TileEntityMachineTransporter.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileEntityMachineTransporter();
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config) {
		super.onConfigChanged(config);

		TileEntityMachineTransporter.MATTER_PER_TRANSPORT = config.getMachineInt(getTranslationKey(),
				"matter_per_entity", 25, "Amount of matter to use per entity when transporting");

		TileEntityMachineTransporter.ENERGY_PER_UNIT = config.getMachineInt(getTranslationKey(), "energy_per_unit", 16,
				"Amount of energy to use per unit distance travelled when transporting");
	}
}