
package matteroverdrive.api;

import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.dialog.IDialogRegistry;
import matteroverdrive.api.matter.IMatterRegistry;
import matteroverdrive.api.renderer.IBionicPartRenderRegistry;
import matteroverdrive.api.starmap.IStarmapRenderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface MatterOverdriveAPI {
    static MatterOverdriveAPI getInstance() {
        return MOApi.instance();
    }

    IMatterRegistry getMatterRegistry();

    IAndroidStatRegistry getAndroidStatRegistry();

    IDialogRegistry getDialogRegistry();

    @SideOnly(Side.CLIENT)
    IAndroidStatRenderRegistry getAndroidStatRenderRegistry();

    @SideOnly(Side.CLIENT)
    IBionicPartRenderRegistry getBionicStatRenderRegistry();

    @SideOnly(Side.CLIENT)
    IStarmapRenderRegistry getStarmapRenderRegistry();
}
