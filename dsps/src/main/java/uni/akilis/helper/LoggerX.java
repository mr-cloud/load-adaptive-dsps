package uni.akilis.helper;
/**
 * 记日志、调试的专用logger
 * @author Akilis
 *
 */
public class LoggerX {
	private static final boolean LOG_ON = true;
	private static boolean LOG_STATUS = LOG_ON;
	
	public static int ERROR = 1;
	public static int WARNING = 2;
	public static int DEBUG = 3;
	public static int INFO = 4;
    
	public static int LEVEL = WARNING;
	
	/**
	 * 输出日志到控制台
	 * @param msg 想要输出的日志信息
	 */
	public static void print(Object msg){
		if(LOG_STATUS){
			System.out.print(msg);
		}
		else
			;
	}
	/**
	 * 输出日志到控制台
	 * @param tag 调用日志的标签，用以标记调用者信息
	 * @param msg 想要输出的日志信息
	 */
	public static void print(String tag, Object msg){
		print(tag + ": ");
		print(msg);
	}
	/**
	 * 输出日志到控制台，并换行
	 * @param msg 想要输出的日志信息
	 */
	public static void println(Object msg){
		print(msg + "\n");
	}
	/**
	 * 输出日志到控制台，并换行
	 * @param tag 调用日志的标签，用以标记调用者信息
	 * @param msg 想要输出的日志信息
	 */
	public static void println(String tag, Object msg){
		print(tag, msg);
		print("\n");
	}
	
	/**
	 * 输出错误日志到控制台并换行
	 * @param msg 想要输出的日志信息
	 */
	public static void error(Object msg){
		System.err.println(msg);
	}
	/**
	 * 输出错误日志到控制台
	 * @param tag 调用日志的标签，用以标记调用者信息
	 * @param msg 想要输出的日志信息
	 */
	public static void error(String tag, Object msg){
		error(tag + ": " + msg);
		error(msg);
	}
	public static void println() {
		print("\n");
	}
	
	/**
	 * Debug 
	 * @param tag
	 * @param msg
	 */
	public static void debug(String tag, Object msg){
	    if (LEVEL >= DEBUG) {
	        println(tag, msg);
	    }
	}
	/**
	 * Debug
	 * @param msg
	 */
	public static void debug(Object msg){
	    if (LEVEL >= DEBUG) {
	        println(msg);
	    }
	}
}
