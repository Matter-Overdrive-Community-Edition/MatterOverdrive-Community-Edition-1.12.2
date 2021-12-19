
package matteroverdrive.core;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.MatterOverdriveAPI;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.matter.IMatterRegistry;
import matteroverdrive.api.renderer.IBionicPartRenderRegistry;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import matteroverdrive.proxy.ClientProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MOAPIInternal implements MatterOverdriveAPI {
    public static final MOAPIInternal INSTANCE = new MOAPIInternal();

    @Override
    public IMatterRegistry getMatterRegistry() {
        return MatterOverdrive.MATTER_REGISTRY;
    }

    @Override
    public IAndroidStatRegistry getAndroidStatRegistry() {
        return MatterOverdrive.STAT_REGISTRY;
    }

    @Override
    public IDialogRegistry getDialogRegistry() {
        return MatterOverdrive.DIALOG_REGISTRY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IAndroidStatRenderRegistry getAndroidStatRenderRegistry() {
        return ClientProxy.renderHandler.getStatRenderRegistry();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBionicPartRenderRegistry getBionicStatRenderRegistry() {
        return ClientProxy.renderHandler.getBionicPartRenderRegistry();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IStarmapRenderRegistry getStarmapRenderRegistry() {
        return ClientProxy.renderHandler.getStarmapRenderRegistry();
    }
}
