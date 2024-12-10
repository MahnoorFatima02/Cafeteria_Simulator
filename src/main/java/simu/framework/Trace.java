package simu.framework;

/**
 * The {@code Trace} class provides general output for diagnostic messages.
 * Each diagnostic message has a severity level, and it is possible to control which level of diagnostic messages is printed.
 */
public class Trace {
	/**
	 * Enum representing the severity levels of diagnostic messages.
	 */
	public enum Level {INFO, WAR, ERR}

	private static Level traceLevel;

	/**
	 * Sets the trace level. Only messages with a severity level equal to or higher than this level will be printed.
	 *
	 * @param lvl the trace level to be set
	 */
	public static void setTraceLevel(Level lvl) {
		traceLevel = lvl;
	}

	/**
	 * Prints a diagnostic message if its severity level is equal to or higher than the current trace level.
	 *
	 * @param lvl the severity level of the message
	 * @param txt the diagnostic message to be printed
	 */
	public static void out(Level lvl, String txt) {
		if (lvl.ordinal() >= traceLevel.ordinal()) {
			System.out.println(txt);
		}
	}
}