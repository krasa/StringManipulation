package osmedile.intellij.stringmanip.styles;

import java.util.Set;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static osmedile.intellij.stringmanip.utils.StringUtil.*;

public enum Style {
	KEBAB_LOWERCASE("kebab-case", "foo-bar") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
			return replaceSeparator(s1, '_', '-');
		}
	},
	KEBAB_UPPERCASE("KEBAB-UPPERCASE", "FOO-BAR") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s);
			return replaceSeparator(s1, '_', '-');
		}
	},
	SNAKE_CASE("snake_case", "foo_bar") {
		@Override
		public String transform(Style style, String s) {
			return wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
		}
	},
	SCREAMING_SNAKE_CASE("SCREAMING_SNAKE_CASE", "FOO_BAR") {
		@Override
		public String transform(Style style, String s) {
			return wordsAndHyphenAndCamelToConstantCase(s);
		}
	},
	PASCAL_CASE("PascalCase", "FooBar", "FooBar") {
		@Override
		public String transform(Style style, String s) {
			if (style != CAMEL_CASE) {
				s = CAMEL_CASE.transform(s);
			}
			return capitalizeFirstWord2(s);
		}
	},
	CAMEL_CASE("camelCase", "fooBar", "fooBar") {
		@Override
		public String transform(Style style, String s) {
			if (style == CAMEL_CASE) {
				return s.trim();
			}
			return toCamelCase(s);
		}
	},
	DOT("dot.case", "foo.bar", "foo.Bar") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
			return replaceSeparator(s1, '_', '.');
		}
	},
	WORD_LOWERCASE("words lowercase", "foo bar") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
			return replaceSeparator(s1, '_', ' ');
		}
	},
	SENTENCE_CASE("First word capitalized", "Foo bar") {
		@Override
		public String transform(Style style, String s) {
			if (style != WORD_LOWERCASE) {
				s = WORD_LOWERCASE.transform(style, s);
			}

			return capitalizeFirstWord(s, Constants.DELIMITERS);
		}
	},
	WORD_CAPITALIZED("Words Capitalized", "Foo Bar") {
		@Override
		public String transform(Style style, String s) {
			if (style != WORD_LOWERCASE && style != WORD_CAPITALIZED) {
				s = WORD_LOWERCASE.transform(style, s);
			}
//			return WordUtils.capitalize(s, Constants.DELIMITERS);
			return capitalizeFirstWord2(s);
		}
	},
	/**
	 * never use that to transform
	 */
	_SINGLE_WORD_CAPITALIZED("<Singlewordcapitalized>", "Foobar") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	/**
	 * never use that to transform
	 */
	_ALL_UPPER_CASE("<ALLUPPERCASE>", "FOOBAR") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	/**
	 * never use that to transform
	 */
	_UNKNOWN("<unknown>") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	;


	/**
	 * first one is how it should look after transformation, others are variations which follows the rule
	 */
	public String[] example;
	private String presentableName;

	Style(String presentableName, String... example) {
		this.presentableName = presentableName;
		this.example = example;
	}

	public String getPresentableName() {
		return presentableName;
	}

	public abstract String transform(Style style, String s);

	public String transform(String s) {
		Style from = from(s);
		String transform = this.transform(from, s);
		return transform;
	}

	public static Style from(String s) {
		s = removeBorderQuotes(s).trim();
		boolean underscore = s.contains("_");
		boolean noUpperCase = noUpperCase(s);
		boolean noLowerCase = noLowerCase(s);
//		boolean containsOnlyLettersAndDigits = containsOnlyLettersAndDigits(s);
		boolean noSeparators = noSeparators(s, '.', '-', '_', ' ');
		boolean noSpecialSeparators = noSeparators(s, '.', '-', '_');
		boolean containsUpperCase = containsUpperCase(s);
		boolean noSpace = !s.contains(" ");

		if (underscore && noUpperCase && noSpace) {
			return SNAKE_CASE;
		}
		if (underscore && noLowerCase && noSpace) {
			return SCREAMING_SNAKE_CASE;
		}

		boolean hyphen = s.contains("-");
		if (hyphen && noUpperCase && noSpace) {
			return KEBAB_LOWERCASE;
		}
		if (hyphen && noLowerCase && noSpace) {
			return KEBAB_UPPERCASE;
		}

		boolean containsDot = s.contains(".");
		if (containsDot && noSpace) {
			return DOT;
		}

		if (noLowerCase) {
			return _ALL_UPPER_CASE;
		}

		boolean startsWithUppercase = startsWithUppercase(s);
		if (startsWithUppercase && noSeparators && !containsUpperCase(s.substring(1, s.length()))) {
			return _SINGLE_WORD_CAPITALIZED;
		}
		if (startsWithUppercase && noSeparators) {
			return PASCAL_CASE;
		}
		if (containsUpperCase && noSeparators) {
			return CAMEL_CASE;
		}

		if (noUpperCase && noSpecialSeparators) {
			return WORD_LOWERCASE;
		}
		if (isCapitalizedFirstButNotAll(s) && !noSpace) {
			return SENTENCE_CASE;
		}
		if (startsWithUppercase && !noSpace) {
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


	private static boolean noSeparators(String str, char... delimiters) {
		if (str.length() == 0) {
			return false;
		}
		Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
		int strLen = str.length();
		int index = 0;

		while (index < strLen) {
			int codePoint = str.codePointAt(index);
			if (delimiterSet.contains(codePoint)) {
				return false;
			}
			index += Character.charCount(codePoint);
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


	static boolean isCapitalizedFirstButNotAll(String str) {
		if (str.length() == 0) {
			return false;
		}
		Set<Integer> delimiterSet = generateDelimiterSet(new char[]{' '});
		int strLen = str.length();
		int index = 0;

		int firstCapitalizedIndex = -1;
		boolean someUncapitalized = false;
		boolean afterSeparatorOrFirst = true;
		while (index < strLen) {
			int codePoint = str.codePointAt(index);
//			char c = str.charAt(index);
			if (delimiterSet.contains(codePoint)) {
				afterSeparatorOrFirst = true;
			} else {
				if (isLowerCase(codePoint) && afterSeparatorOrFirst) {
					if (firstCapitalizedIndex == -1) {
						return false;
					}
					someUncapitalized = true;
					afterSeparatorOrFirst = false;
				} else if (isUpperCase(codePoint) && afterSeparatorOrFirst) {
					if (firstCapitalizedIndex == -1) {
						firstCapitalizedIndex = index;
					}
					afterSeparatorOrFirst = false;
				}
			}
			index += Character.charCount(codePoint);
		}
		return firstCapitalizedIndex != -1 && someUncapitalized;
	}

	private static boolean startsWithUppercase(String str) {
		if (str.length() == 0) {
			return false;
		}
		int strLen = str.length();
		int index = 0;
		while (index < strLen) {
			int codePoint = str.codePointAt(index);
			if (isLowerCase(codePoint)) {
				return false;
			}
			if (isUpperCase(codePoint)) {
				return true;
			}
			index += Character.charCount(codePoint);
		}
		return false;
	}

	private static class Constants {
		private static final char[] DELIMITERS = new char[]{'\'', '\"', ' '};
	}
}
