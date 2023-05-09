package fp.utils;

public class ComparableUtils {
	public static <T extends Comparable<T>> T min(T left, T right) {
		return left.compareTo(right) < 0 ? left : right;
	}

	public static <T extends Comparable<T>> T max(T left, T right) {
		return left.compareTo(right) > 0 ? left : right;
	}
}
