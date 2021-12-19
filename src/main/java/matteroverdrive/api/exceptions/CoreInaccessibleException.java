
package matteroverdrive.api.exceptions;

public class CoreInaccessibleException extends RuntimeException {
    public CoreInaccessibleException(String message, Object... params) {
        super(String.format(message, params));
    }
}
