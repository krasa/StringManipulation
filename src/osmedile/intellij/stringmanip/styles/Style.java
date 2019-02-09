package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.utils.StringUtil;

import static org.apache.commons.lang.WordUtils.capitalize;
import static osmedile.intellij.stringmanip.utils.StringUtil.*;

public enum Style {
	HYPHEN_LOWERCASE("foo-bar") {
		@Override
		public String transform(Style style, String s) {
			if (style == HYPHEN_UPPERCASE) {
				return s.toLowerCase();
			}
			if (style == SCREAMING_SNAKE_CASE) {
				s = s.toLowerCase();
			}
			if (style == CAMEL_CASE || style == PASCAL_CASE || style == _UNKNOWN) {
				s = camelToText(s);
			}
			return StringUtil.wordsToHyphenCase(s);
		}
	},
	HYPHEN_UPPERCASE("FOO-BAR") {
		@Override
		public String transform(Style style, String s) {
			if (style == _ALL_UPPER_CASE) {
				s = CAMEL_CASE.transform(style, s);
				style = CAMEL_CASE;
			}
			if (style == CAMEL_CASE || style == PASCAL_CASE) {
				s = camelToText(s);
			}
			return StringUtil.wordsToHyphenCase(s).toUpperCase();
		}
	},
	UNDERSCORE_LOWERCASE("foo_bar") {
		@Override
		public String transform(Style style, String s) {
			return wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
		}
	},
	SCREAMING_SNAKE_CASE("FOO_BAR") {
		@Override
		public String transform(Style style, String s) {
			return wordsAndHyphenAndCamelToConstantCase(s);
		}
	},
	PASCAL_CASE("FooBar", "FooBar") {
		@Override
		public String transform(Style style, String s) {
			if (style != CAMEL_CASE) {
				s = CAMEL_CASE.transform(s);
			}
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		}
	},
	CAMEL_CASE("fooBar", "fooBar") {
		@Override
		public String transform(Style style, String s) {
			if (style == CAMEL_CASE) {
				return s.trim();
			}
			s = s.replace("-", "_");
			s = s.replace(".", "_");
			return toCamelCase(s);
		}
	},
	DOT("foo.bar", "foo.Bar") {
		@Override
		public String transform(Style style, String s) {
			return StringUtil.toDotCase(s);
		}
	},
	WORD_LOWERCASE("foo bar") {
		@Override
		public String transform(Style style, String s) {
			if (style == _ALL_UPPER_CASE || style == DOT || style == HYPHEN_LOWERCASE || style == HYPHEN_UPPERCASE || style == UNDERSCORE_LOWERCASE
				|| style == SCREAMING_SNAKE_CASE) {
				s = CAMEL_CASE.transform(style, s);
			}
			return camelToText(s);
		}
	},
	WORD_CAPITALIZED("Foo Bar") {
		@Override
		public String transform(Style style, String s) {
			if (style != WORD_LOWERCASE && style != WORD_CAPITALIZED) {
				s = WORD_LOWERCASE.transform(style, s);
			}
			return capitalize(s, Constants.DELIMITERS);
		}
	},
	/**
	 * never use that to transform
	 */
	_SINGLE_WORD_CAPITALIZED("Foobar") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	/**
	 * never use that to transform
	 */
	_ALL_UPPER_CASE("FOOBAR") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	/**
	 * never use that to transform
	 */
	_UNKNOWN() {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},;

	/**
	 * first one is how it should look after transformation, others are variations which follows the rule
	 */
	public String[] example;

	Style(String... example) {
		this.example = example;
	}

	public abstract String transform(Style style, String s);

	public String transform(String s) {
		return this.transform(from(s), s);
	}

	public static Style from(String s) {
		s = removeBorderQuotes(s);
		boolean underscore = s.contains("_");
		boolean noUpperCase = noUpperCase(s);
		boolean noLowerCase = noLowerCase(s);
		boolean containsOnlyLettersAndDigits = containsOnlyLettersAndDigits(s);
		boolean containsUpperCase = containsUpperCase(s);

		if (underscore && noUpperCase) {
			return UNDERSCORE_LOWERCASE;
		}
		if (underscore && noLowerCase) {
			return SCREAMING_SNAKE_CASE;
		}

		boolean hyphen = s.contains("-");
		if (hyphen && noUpperCase) {
			return HYPHEN_LOWERCASE;
		}
		if (hyphen && noLowerCase) {
			return HYPHEN_UPPERCASE;
		}

		boolean containsDot = s.contains(".");
		if (containsDot) {
			return DOT;
		}

		if (noLowerCase) {
			return _ALL_UPPER_CASE;
		}

		boolean startsWithUppercase = startsWithUppercase(s);
		if (startsWithUppercase && containsOnlyLettersAndDigits && !containsUpperCase(s.substring(1, s.length()))) {
			return _SINGLE_WORD_CAPITALIZED;
		}
		if (startsWithUppercase && containsOnlyLettersAndDigits) {
			return PASCAL_CASE;
		}
		if (containsUpperCase && containsOnlyLettersAndDigits) {
			return CAMEL_CASE;
		}

		if (noUpperCase) {
			return WORD_LOWERCASE;
		}
		if (startsWithUppercase) {
			return WORD_CAPITALIZED;
		}
		return _UNKNOWN;
	}

	private static boolean containsOnlyLettersAndDigits(String s) {
		for (char c : s.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private static boolean noUpperCase(String s) {
		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c)) {
				return false;
			}
		}
		return true;
	}

	private static String removeBorderQuotes(String s) {
		if (isQuoted(s)) {
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}

	public static boolean isQuoted(String selectedText) {
		return selectedText != null && selectedText.length() > 2
			&& (Style.isBorderChar(selectedText, "\"") || Style.isBorderChar(selectedText, "\'"));
	}

	public static boolean isBorderChar(String s, String borderChar) {
		return s.startsWith(borderChar) && s.endsWith(borderChar);
	}

	public static boolean noLowerCase(String s) {
		for (char c : s.toCharArray()) {
			if (Character.isLowerCase(c)) {
				return false;
			}
		}
		return true;
	}

	private static boolean containsUpperCase(String s) {
		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}


	private static boolean startsWithUppercase(String s) {
		if (s.length() == 0) {
			return false;
		}
		return Character.isUpperCase(s.charAt(0));
	}

	private static class Constants {
		private static final char[] DELIMITERS = new char[]{'\'', '\"', ' '};
	}
}
