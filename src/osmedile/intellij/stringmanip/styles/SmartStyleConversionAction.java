package osmedile.intellij.stringmanip.styles;

import osmedile.intellij.stringmanip.AbstractStringManipAction;

import static org.apache.commons.lang.WordUtils.capitalize;
import static osmedile.intellij.stringmanip.styles.SmartStyleConversionAction.Style.transformation;
import static osmedile.intellij.stringmanip.utils.StringUtil.*;

public class SmartStyleConversionAction extends AbstractStringManipAction {
    public SmartStyleConversionAction() {
    }

    public SmartStyleConversionAction(boolean setupHandler) {
        super(setupHandler);
    }

    public enum Style {
        LOWERCASE_HYPHEN("foo-bar") {
            @Override
            public String transform(Style style, String s) {
                if (style == HYPHEN) {
                    return s.toLowerCase();
                }
                if (style == UPPERCASE_UNDERSCORE) {
                    s = s.toLowerCase();
                }
                if (style == CAMELCASE) {
                    s = camelToText(s);
                }
                return ToHyphenCaseAction.wordsToHyphenCase(s);
            }
        },
        HYPHEN("FOO-BAR", "foo-Bar") {
            @Override
            public String transform(Style style, String s) {
                if (style == CAMELCASE) {
                    s = camelToText(s);
                }
                return ToHyphenCaseAction.wordsToHyphenCase(s).toUpperCase();
            }
        },
        LOWERCASE_UNDERSCORE("foo_bar") {
            @Override
            public String transform(Style style, String s) {
                return wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
            }
        },
        UPPERCASE_UNDERSCORE("FOO_BAR", "foo_Bar") {
            @Override
            public String transform(Style style, String s) {
                return wordsAndHyphenAndCamelToConstantCase(s);
            }
        },
        CAMELCASE("fooBar", " fooBar") {
            @Override
            public String transform(Style style, String s) {
                if (style == CAMELCASE) {
                    return s.trim();
                }
                s = s.replace("-", "_");
                return toCamelCase(s);
            }
        },
        WORD_LOWERCASE("foo bar", " foo bar") {
            @Override
            public String transform(Style style, String s) {
                if (style == LOWERCASE_HYPHEN || style == HYPHEN || style == LOWERCASE_UNDERSCORE || style == UPPERCASE_UNDERSCORE) {
                    s = CAMELCASE.transform(style, s);
                }
                return camelToText(s);
            }
        },
        CAPITALIZED("Foo Bar", " Foo Bar") {
            @Override
            public String transform(Style style, String s) {
                return capitalize(s);
            }
        },;

        public static Style[][] transformation = new Style[][]{
                {LOWERCASE_HYPHEN, HYPHEN},
                {HYPHEN, LOWERCASE_UNDERSCORE},
                {LOWERCASE_UNDERSCORE, UPPERCASE_UNDERSCORE},
                {UPPERCASE_UNDERSCORE, WORD_LOWERCASE},
                {WORD_LOWERCASE, CAPITALIZED},
                {CAPITALIZED, CAMELCASE},
                {CAMELCASE, LOWERCASE_HYPHEN},
        };

        /**
         * first one is how it should look after transformation, others are variations which follows the rule
         */
        public String[] example;

        Style(String... example) {
            this.example = example;
        }

        public abstract String transform(Style style, String s);

        public static Style from(String s) {
            boolean underscore = s.contains("_");
            boolean containsLowerCase = containsLowerCase(s);
            if (underscore && containsLowerCase) {
                return LOWERCASE_UNDERSCORE;
            }
            if (underscore) {
                return UPPERCASE_UNDERSCORE;
            }

            boolean containsUpperCase = containsUpperCase(s);
            boolean onlyLowercase = !containsUpperCase;
            boolean hyphen = s.contains("-");
            if (hyphen && onlyLowercase) {
                return LOWERCASE_HYPHEN;
            }
            if (hyphen) {
                return HYPHEN;
            }

            boolean containsWhitespace = s.contains(" ");
            if (!containsWhitespace) {
                return CAMELCASE;
            }


            if (onlyLowercase) {
                return WORD_LOWERCASE;
            }
            if (containsUpperCase) {
                return CAPITALIZED;
            }
            throw new IllegalStateException("cannot determine style for: " + s);
        }
    }

    public String transform(String s) {
        Style style = Style.from(s);
        for (Style[] styles : transformation) {
            if (styles[0] == style) {
                return styles[1].transform(styles[0], s);
            }
        }
        return s;
    }


    private static boolean containsUpperCase(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsLowerCase(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }
}
