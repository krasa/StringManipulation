package osmedile.intellij.stringmanip.escaping;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class UnescapeCSharpTest {
    //
    // Data
    //

    @DataPoints("regular")
    public static String[][] regular = new String[][]{
            // Noop
            {"", ""},
            {"All your base are belong to us", "All your base are belong to us"},
            {"{{}}", "{{}}"},

            // Unescape
            {"\\u0000\\u007F\\uDEAD\\uBEEF\\uFFFF", "\0\u007F\uDEAD\uBEEF\uFFFF"},
            {"\\0\\a\\b\\f\\t\\v\\n\\r\\\"\\'", "\0\u0007\b\f\t\u000B\n\r\"'"},
            {"\\udEaD\\uBeEf", "\uDEAD\uBEEF"}, // lower/upper

            // Fail to unescape
            {"\\U0000\\A\\B\\F\\N\\R\\T\\V", "\\U0000\\A\\B\\F\\N\\R\\T\\V"}, // no upper escapes
            {"\\u0\\u00\\u000", "\\u0\\u00\\u000"}, // too short unicode escape
            {"\\0\\u0", "\0\\u0"}, // too few at end
            {"\\0\\u00", "\0\\u00"}, // too few at end
            {"\\0\\u000", "\0\\u000"}, // too few at end
    };

    @DataPoints("verbatim")
    public static String[][] verbatim = new String[][]{
            // Noop
            {"", ""},
            {"All your base are belong to us", "All your base are belong to us"},
            {"\\0\\t\\n\\r\\uDEAD\\uBEEF{}", "\\0\\t\\n\\r\\uDEAD\\uBEEF{}"},
            {"{{}}", "{{}}"},

            // Unescape
            {"\"\"", "\""},
            {"\"\"\"\"\"\"", "\"\"\""},

            // Fail to unescape
            {"\"blah", "\"blah"}, // single quote
            {"\"", "\""}, // single quote at end
    };

    @DataPoints("interpolated")
    public static String[][] interpolated = new String[][]{
            // Noop
            {"", ""},
            {"All your base are belong to us", "All your base are belong to us"},

            // Unescape
            {"{{}}", "{}"},
            {"}}}}}}{{{{{{", "}}}{{{"},
            {"\\u0000\\u007F\\uDEAD\\uBEEF\\uFFFF", "\0\u007F\uDEAD\uBEEF\uFFFF"},
            {"\\0\\a\\b\\f\\t\\v\\n\\r\\\"\\'{}", "\0\u0007\b\f\t\u000B\n\r\"'{}"},
            {"\\udEaD\\uBeEf", "\uDEAD\uBEEF"}, // lower/upper

            // Fail to unescape
            {"\\U0000\\A\\B\\F\\N\\R\\T\\V", "\\U0000\\A\\B\\F\\N\\R\\T\\V"}, // no upper escapes
            {"\\u0\\u00\\u000", "\\u0\\u00\\u000"}, // too short unicode escape
            {"\\0\\u0", "\0\\u0"}, // too few at end
            {"\\0\\u00", "\0\\u00"}, // too few at end
            {"\\0\\u000", "\0\\u000"}, // too few at end
            {"{blah", "{blah"}, // single brace
            {"}blah", "}blah"}, // single brace
            {"{", "{"}, // single brace at end
            {"}", "}"}, // single brace at end
    };

    @DataPoints("interpolatedVerbatim")
    public static String[][] interpolatedVerbatim = new String[][]{
            // Noop
            {"", ""},
            {"All your base are belong to us", "All your base are belong to us"},
            {"\\0\\t\\n\\r\\uDEAD\\uBEEF{}", "\\0\\t\\n\\r\\uDEAD\\uBEEF{}"},

            // Unescape
            {"{{}}", "{}"},
            {"}}}}}}{{{{{{", "}}}{{{"},
            {"\"\"", "\""},
            {"\"\"\"\"\"\"", "\"\"\""},

            // Fail to unescape
            {"{blah", "{blah"}, // single brace
            {"}blah", "}blah"}, // single brace
            {"{", "{"}, // single brace at end
            {"}", "}"}, // single brace at end
            {"\"blah", "\"blah"}, // single quote
            {"\"", "\""}, // single quote at end
    };

    //
    // Tests
    //

    @Theory
    public void unescapeRegular(@FromDataPoints("regular") String[] escapedRaw) {
        UnescapeCSharpRegularAction action = new UnescapeCSharpRegularAction();
        assertEquals(escapedRaw[1], action.test_transformByLine(escapedRaw[0]));
    }

    @Theory
    public void unescapeVerbatim(@FromDataPoints("verbatim") String[] escapedRaw) {
        UnescapeCSharpVerbatimAction action = new UnescapeCSharpVerbatimAction();
		assertEquals(escapedRaw[1], action.test_transformByLine(escapedRaw[0]));
    }

    @Theory
    public void unescapeInterpolated(@FromDataPoints("interpolated") String[] escapedRaw) {
        UnescapeCSharpInterpolatedAction action = new UnescapeCSharpInterpolatedAction();
		assertEquals(escapedRaw[1], action.test_transformByLine(escapedRaw[0]));
    }

    @Theory
    public void unescapeInterpolatedVerbatim(@FromDataPoints("interpolatedVerbatim") String[] escapedRaw) {
        UnescapeCSharpInterpolatedVerbatimAction action = new UnescapeCSharpInterpolatedVerbatimAction();
		assertEquals(escapedRaw[1], action.test_transformByLine(escapedRaw[0]));
    }
}
