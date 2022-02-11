package osmedile.intellij.stringmanip.styles;

import org.apache.commons.text.WordUtils;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.escaping.DiacriticsToAsciiAction;
import osmedile.intellij.stringmanip.styles.custom.DefaultActions;

import static osmedile.intellij.stringmanip.utils.StringUtil.*;

/**
 * Finite state machine.
 *
 * @see DefaultActions.Helper#getDefaultSteps()
 * @see PluginPersistentStateComponent#fixActions()
 */
public enum Style {
	KEBAB_LOWERCASE("kebab-case", true, "foo-bar") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
			return replaceSeparator(s1, '_', '-');
		}
	},
	KEBAB_UPPERCASE("KEBAB-UPPERCASE", true, "FOO-BAR") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s);
			return replaceSeparator(s1, '_', '-');
		}
	},
	SNAKE_CASE("snake_case", true, "foo_bar") {
		@Override
		public String transform(Style style, String s) {
			if (style == SNAKE_CASE || style == CAPITALIZED_SNAKE_CASE || style == SCREAMING_SNAKE_CASE) {
				return s.toLowerCase();
			}
			return wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
		}
	},
	CAPITALIZED_SNAKE_CASE("Capitalized_Snake_Case", true, "Foo_Bar") {
		@Override
		public String transform(Style style, String s) {
			return WordUtils.capitalizeFully(SNAKE_CASE.transform(style, s), '_');
		}
	},
	SCREAMING_SNAKE_CASE("SCREAMING_SNAKE_CASE", true, "FOO_BAR") {
		@Override
		public String transform(Style style, String s) {
			return wordsAndHyphenAndCamelToConstantCase(s);
		}
	},
	PASCAL_CASE("PascalCase", true, "FooBar", "FooBar") {
		@Override
		public String transform(Style style, String s) {
			if (style != CAMEL_CASE) {
				s = CAMEL_CASE.transform(s);
			}
			return capitalizeFirstWord2(s);
		}
	},
	CAMEL_CASE("camelCase", true, "fooBar", "fooBar") {
		@Override
		public String transform(Style style, String s) {
			if (style == CAMEL_CASE) {
				return s.trim();
			}
			return toCamelCase(s);
		}
	},
	DOT("dot.case", true, "foo.bar", "foo.Bar") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
			return replaceSeparator(s1, '_', '.');
		}
	},
	WORD_LOWERCASE("words lowercase", false, "foo bar") {
		@Override
		public String transform(Style style, String s) {
			String s1 = wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
			return replaceSeparator(s1, '_', ' ');
		}
	},
	SENTENCE_CASE("First word capitalized", false, "Foo bar") {
		@Override
		public String transform(Style style, String s) {
			if (style != WORD_LOWERCASE) {
				s = WORD_LOWERCASE.transform(style, s);
			}

			return capitalizeFirstWord(s, Constants.DELIMITERS);
		}
	},
	WORD_CAPITALIZED("Words Capitalized", false, "Foo Bar") {
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
	_SINGLE_WORD_CAPITALIZED("<Singlewordcapitalized>", false, "Foobar") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	/**
	 * never use that to transform
	 */
	_ALL_UPPER_CASE("<ALLUPPERCASE>", false, "FOOBAR") {
		@Override
		public String transform(Style style, String s) {
			return s;
		}
	},
	/**
	 * never use that to transform
	 */
	_UNKNOWN("<unknown>", false) {
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
	private boolean normalize;

	Style(String presentableName, boolean normalize, String... example) {
		this.presentableName = presentableName;
		this.example = example;
		this.normalize = normalize;
	}

	public String getPresentableName() {
		return presentableName;
	}

	protected abstract String transform(Style style, String s);

	public String transform(String s) {
		Style from = from(s);
		String transform = this.transform(from, s);
		if (normalize && PluginPersistentStateComponent.getInstance().isNormalizeCaseSwitching()) {
			transform = DiacriticsToAsciiAction.toPlain(transform);
		}
		return transform;
	}

	public static Style from(String s) {
		s = removeBorderQuotes(s).trim();
		boolean underscore = containsSeparatorBetweenLetters(s, '_');
		boolean noUpperCase = noUpperCase(s);
		boolean noLowerCase = noLowerCase(s);
//		boolean containsOnlyLettersAndDigits = containsOnlyLettersAndDigits(s);
		boolean noSeparators = noSeparators(s, '.', '-', '_', ' ');
		boolean noSpecialSeparators = noSeparators(s, '.', '-', '_');
		boolean containsUpperCaseAfterLowerCase = containsUpperCaseAfterLowerCase(s);
		boolean noSpace = noSeparators(s, ' ');
		boolean dot = containsSeparatorBetweenLetters(s, '.');
		boolean hyphen = containsSeparatorBetweenLetters(s, '-');
		boolean noDot = !dot;
		boolean noHyphen = !hyphen;
		boolean noUnderscore = !underscore;

		if (underscore && noUpperCase && noSpace && noDot && noHyphen) {
			return SNAKE_CASE;
		}
		if (underscore && noLowerCase && noSpace && noDot && noHyphen) {
			return SCREAMING_SNAKE_CASE;
		}
		if (underscore && noSpace && noDot && noHyphen && isCapitalizedFully(s, '_')) {
			return CAPITALIZED_SNAKE_CASE;
		}

		if (hyphen && noUpperCase && noSpace && noDot && noUnderscore) {
			return KEBAB_LOWERCASE;
		}
		if (hyphen && noLowerCase && noSpace && noDot && noUnderscore) {
			return KEBAB_UPPERCASE;
		}

		if (dot && noSpace && noUnderscore && noHyphen) {
			return DOT;
		}

		if (noLowerCase) {
			return _ALL_UPPER_CASE;
		}

		boolean firstLetterUppercase = firstLetterUpperCase(s);
		if (firstLetterUppercase && noSeparators && !containsUpperCase(s.substring(1))) {
			return _SINGLE_WORD_CAPITALIZED;
		}
		if (firstLetterUppercase && noSeparators && noSpace && containsUpperCase(s.substring(1))) {
			return PASCAL_CASE;
		}
		boolean firstLetterLowercase = firstLetterLowerCase(s);
		if (firstLetterLowercase && containsUpperCaseAfterLowerCase && noSeparators && noSpace) {
			return CAMEL_CASE;
		}

		if (noUpperCase && noSpecialSeparators) {
			return WORD_LOWERCASE;
		}
		if (isCapitalizedFirstButNotAll(s) && !noSpace) {
			return SENTENCE_CASE;
		}
		if (firstLetterUppercase && !noSpace) {
			return WORD_CAPITALIZED;
		}
		return _UNKNOWN;
	}


}
