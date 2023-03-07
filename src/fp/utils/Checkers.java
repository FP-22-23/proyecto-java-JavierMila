package fp.utils;

public class Checkers {
	
	public static void check(String textoRestricción, Boolean condición) {
		if(!condición) {
			throw new IllegalArgumentException(
					Thread.currentThread().getStackTrace()[2].getClassName()
					+ "." + 
					Thread.currentThread().getStackTrace()[2].getMethodName()
					+ ":" + textoRestricción);
		}
	}

}
