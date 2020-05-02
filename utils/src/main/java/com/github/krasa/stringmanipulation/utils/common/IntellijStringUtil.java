package com.github.krasa.stringmanipulation.utils.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class IntellijStringUtil {
	/**
	 * Capitalize the first letter of the sentence.
	 */
	public static @NotNull String capitalize(@NotNull String s) {
		if (s.isEmpty()) return s;
		if (s.length() == 1) return toUpperCase(s);

		// Optimization
		if (Character.isUpperCase(s.charAt(0))) return s;
		return toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public static String toUpperCase(String s) {
		return s == null ? null : s.toUpperCase(Locale.ENGLISH);
	}

	public static char toUpperCase(char a) {
		if (a < 'a') return a;
		if (a <= 'z') return (char) (a + ('A' - 'a'));
		return Character.toUpperCase(a);
	}      /**
	   * Allows to answer if given symbol is white space, tabulation or line feed.
	   *
	   * @param c symbol to check
	   * @return {@code true} if given symbol is white space, tabulation or line feed; {@code false} otherwise
	   */
	  @Contract(pure = true)
	  public static boolean isWhiteSpace(char c) {
	    return c == '\n' || c == '\t' || c == ' ';
	  }       @Contract(pure = true)
	    public static @NotNull String trimLeading(@NotNull String string) {
	      return trimLeading((CharSequence)string).toString();
	    }
	    @Contract(pure = true)
	    public static @NotNull CharSequence trimLeading(@NotNull CharSequence string) {
	      int index = 0;
	      while (index < string.length() && Character.isWhitespace(string.charAt(index))) index++;
	      return string.subSequence(index, string.length());
	    }
	  
	    @Contract(pure = true)
	    public static @NotNull String trimLeading(@NotNull String string, char symbol) {
	      int index = 0;
	      while (index < string.length() && string.charAt(index) == symbol) index++;
	      return string.substring(index);
	    }
	  
	    public static @NotNull StringBuilder trimLeading(@NotNull StringBuilder builder, char symbol) {
	      int index = 0;
	      while (index < builder.length() && builder.charAt(index) == symbol) index++;
	      if (index > 0) builder.delete(0, index);
	      return builder;
	    }
}
