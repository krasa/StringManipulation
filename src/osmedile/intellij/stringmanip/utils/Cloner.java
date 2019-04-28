package osmedile.intellij.stringmanip.utils;


public class Cloner {
	public static <T> T deepClone(T o) {
		com.rits.cloning.Cloner cloner = new com.rits.cloning.Cloner();
		cloner.setNullTransient(true);
		T t = cloner.deepClone(o);
		return t;
	}
}
