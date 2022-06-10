
package matteroverdrive.data.dialog;

import matteroverdrive.api.dialog.IDialogMessage;
import matteroverdrive.api.dialog.IDialogNpc;

public abstract class DialogLogic {
	public abstract boolean trigger(IDialogMessage dialogMessage, IDialogNpc dialogNpc);
}
