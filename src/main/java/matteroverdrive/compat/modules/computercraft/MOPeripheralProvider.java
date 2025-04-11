
package matteroverdrive.compat.modules.computercraft;

import javax.annotation.Nonnull;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import net.minecraftforge.fml.common.Optional;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Optional.Interface(modid = "computercraft", iface = "dan200.computercraft.api.peripheral.IPeripheralProvider")
public class MOPeripheralProvider implements IPeripheralProvider
{

	@Override
	@Optional.Method(modid = "computercraft")
	public IPeripheral getPeripheral(@Nonnull final World world, @Nonnull final BlockPos blockPos, @Nonnull final EnumFacing side) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(blockPos));

		if (tileEntity instanceof TileEntityMachineTransporter)
		{
			System.out.println("TileEntityMachineTransporter");
			return (IPeripheral)tileEntity;
		}
		else if (tileEntity instanceof TileEntityMachineFusionReactorController)
		{
			System.out.println("TileEntityMachineFusionReactorController");
			return (IPeripheral)tileEntity;
		}

		return null;
	}

}
