package com.weibo.lodil;

public class LOG {

	public static void debug(String s) {
		print("[DBUG]" + s);
	}

	public static void info(String s) {
		print("[INFO]" + s);
	}
	
	public static void warn(String s) {
		print("[WARN]" + s);
	}

	public static void print(String s) {
		String caller = getCaller();

		System.out.println(caller + " :: " + s.replaceFirst("com.weibo.lodil.mmap.", ""));
	}

	public static String getCaller() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		for (int index = 0; index < stack.length; index++) {
			StackTraceElement ste = stack[index];
			if (ste.getClassName().indexOf("LOG") >= 0){
				continue;
			}

			String result = ste.getClassName().replaceFirst("com.weibo.lodil.mmap.", "") + ":" + ste.getMethodName()
					+ ":" + ste.getLineNumber();
			return result;
		}
		return "";
	}

}
