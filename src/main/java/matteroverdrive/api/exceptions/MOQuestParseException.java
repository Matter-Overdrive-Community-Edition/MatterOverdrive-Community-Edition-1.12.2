
package matteroverdrive.api.exceptions;

public class MOQuestParseException extends MORuntimeException {

	private static final long serialVersionUID = -4603504590591203591L;

	public MOQuestParseException() {
    }

    public MOQuestParseException(String message, Object... params) {
        super(String.format(message, params));
    }

    public MOQuestParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MOQuestParseException(Throwable cause) {
        super(cause);
    }

    public MOQuestParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
