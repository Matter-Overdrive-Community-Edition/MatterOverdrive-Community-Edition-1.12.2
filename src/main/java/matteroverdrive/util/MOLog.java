
package matteroverdrive.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MOLog {
	private static final Logger logger = LogManager.getLogger("MatterOverdrive");

	private MOLog() {
	}

	public static void log(Level level, String format, Object... data) {
		logger.log(level, String.format(format, data));
	}

	public static void log(Level level, Throwable ex, String format, Object... data) {
		logger.log(level, String.format(format, data), ex);
	}

	public static void bigWarning(String format, Object... data) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		log(Level.WARN, "****************************************");
		log(Level.WARN, "* " + format, data);
		for (int i = 2; i < 8 && i < trace.length; i++) {
			log(Level.WARN, "*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
		}
		log(Level.WARN, "****************************************");
	}

	public static void warn(String format, Object... data) {
		log(Level.WARN, format, data);
	}

	public static void info(String format, Object... data) {
		log(Level.INFO, format, data);
	}

	public static void debug(String format, Object... data) {
		log(Level.DEBUG, format, data);
	}

	public static void error(String format, Object... data) {
		log(Level.ERROR, format, data);
	}

	public static void error(String format, Throwable t, Object... data) {
		log(Level.ERROR, t, format, data);
	}

	public static void fatal(String format, Object... data) {
		log(Level.FATAL, format, data);
	}

	public static void fatal(String format, Throwable t, Object... data) {
		log(Level.FATAL, t, format, data);
	}
}
