package com.weibo.lodil;

public class LOG {

	public static void debug(final String s) {
		print("[DBUG]" + s);
	}

	public static void info(final String s) {
		print("[INFO]" + s);
	}

	public static void warn(final String s) {
		print("[WARN]" + s);
	}

	public static void print(final String s) {
		final String caller = getCaller();

		System.out.println(caller + " :: " + s.replaceAll("com.weibo.lodil.mmap.", ""));
	}

	public static String getCaller() {
		final StackTraceElement stack[] = (new Throwable()).getStackTrace();
		for (final StackTraceElement ste : stack) {
			if (ste.getClassName().indexOf("LOG") >= 0){
				continue;
			}

			final String result = ste.getClassName().replaceFirst("com.weibo.lodil.mmap.", "") + ":" + ste.getMethodName()
					+ ":" + ste.getLineNumber();
			return result;
		}
		return "";
	}

}
