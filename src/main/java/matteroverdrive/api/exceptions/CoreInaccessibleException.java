
package matteroverdrive.api.exceptions;

public class CoreInaccessibleException extends RuntimeException {

	private static final long serialVersionUID = 8774633604950854156L;

	public CoreInaccessibleException(String message, Object... params) {
		super(String.format(message, params));
	}
}
