
package matteroverdrive.client.render.conversation;

import matteroverdrive.api.renderer.IDialogShot;

public abstract class DialogShot implements IDialogShot {
    public static final DialogShotClose closeUp = new DialogShotClose(1.5f, 0.3f);
    public static final DialogShotClose dramaticCloseUp = new DialogShotClose(1.2f, 0.3f);
    public static final DialogShotWide wideNormal = new DialogShotWide(0.22f, false, 1);
    public static final DialogShotWide wideOpposite = new DialogShotWide(0.22f, true, 1);
    public static final DialogShotFromBehind fromBehindLeftClose = new DialogShotFromBehind(2, 4);
    public static final DialogShotFromBehind fromBehindLeftFar = new DialogShotFromBehind(3, 4);
    public static final DialogShotFromBehind fromBehindRightClose = new DialogShotFromBehind(2, 8);
    public static final DialogShotFromBehind fromBehindRightFar = new DialogShotFromBehind(3, -8);
}
