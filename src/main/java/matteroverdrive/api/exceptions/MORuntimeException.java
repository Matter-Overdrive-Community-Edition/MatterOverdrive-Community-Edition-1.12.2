
package matteroverdrive.api.exceptions;

public class MORuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2098661208456481956L;

	public MORuntimeException() {
    }

    public MORuntimeException(String message) {
        super(message);
    }

    public MORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MORuntimeException(Throwable cause) {
        super(cause);
    }

    public MORuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
