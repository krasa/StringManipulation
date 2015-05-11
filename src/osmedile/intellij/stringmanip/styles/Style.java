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
            if (style == UNDERSCORE_UPPERCASE) {
                s = s.toLowerCase();
            }
            if (style == CAMEL_CASE) {
                s = camelToText(s);
            }
            return StringUtil.wordsToHyphenCase(s);
        }
    },
    HYPHEN_UPPERCASE("FOO-BAR") {
        @Override
        public String transform(Style style, String s) {
            if (style == CAMEL_CASE) {
                s = camelToText(s);
            }
            return StringUtil.wordsToHyphenCase(s).toUpperCase();
        }
    },
    UNDERSCORE_LOWERCASE("foo_bar") {
        @Override
        public String transform(Style style, String s) {
            s = CAMEL_CASE.transform(style, s);
            return wordsAndHyphenAndCamelToConstantCase(s).toLowerCase();
        }
    },
    UNDERSCORE_UPPERCASE("FOO_BAR") {
        @Override
        public String transform(Style style, String s) {
            s = CAMEL_CASE.transform(style, s);
            return wordsAndHyphenAndCamelToConstantCase(s);
        }
    },
    CAMEL_CASE("fooBar", " fooBar") {
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
    DOT("foo.bar", " foo.Bar") {
        @Override
        public String transform(Style style, String s) {
            return StringUtil.toDotCase(s);
        }
    },
    WORD_LOWERCASE("foo bar") {
        @Override
        public String transform(Style style, String s) {
            if (style == DOT || style == HYPHEN_LOWERCASE || style == HYPHEN_UPPERCASE || style == UNDERSCORE_LOWERCASE || style == UNDERSCORE_UPPERCASE) {
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
            return capitalize(s);
        }
    },
    UNKNOWN() {
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
        boolean underscore = s.contains("_");
        boolean containsLowerCase = containsLowerCase(s);
        if (underscore && containsLowerCase) {
            return UNDERSCORE_LOWERCASE;
        }
        if (underscore) {
            return UNDERSCORE_UPPERCASE;
        }

        boolean containsUpperCase = containsUpperCase(s);
        boolean onlyLowercase = !containsUpperCase;
        boolean hyphen = s.contains("-");
        if (hyphen && onlyLowercase) {
            return HYPHEN_LOWERCASE;
        }
        if (hyphen) {
            return HYPHEN_UPPERCASE;
        }

        boolean containsDot = s.contains(".");
        if (containsDot) {
            return DOT;
        }

        boolean containsWhitespace = s.contains(" ");
        if (!containsWhitespace && containsUpperCase) {
            return CAMEL_CASE;
        }


        if (onlyLowercase) {
            return WORD_LOWERCASE;
        }
        if (startsWithUppercase(s)) {
            return WORD_CAPITALIZED;
        }
        return UNKNOWN;
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

    private static boolean containsLowerCase(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }
}
