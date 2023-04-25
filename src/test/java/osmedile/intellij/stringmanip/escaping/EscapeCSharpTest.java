package osmedile.intellij.stringmanip.escaping;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class EscapeCSharpTest {
    //
    // Data
    //

    public static String[] raw = new String[]{
            "",
            "All your base are belong to us",
            "\0\u0007\b\f\t\u000B\n\r\"{}",
            "\u007F\u0080\uDEAD\uFFFF",
    };

    public static String[] regular = new String[]{
            "",
            "All your base are belong to us",
            "\\0\\a\\b\\f\\t\\v\\n\\r\\\"{}",
            "\u007F\\u0080\\uDEAD\\uFFFF",
    };

    public static String[] verbatim = new String[]{
            "",
            "All your base are belong to us",
            "\0\u0007\b\f\t\u000B\n\r\"\"{}",
            "\u007F\u0080\uDEAD\uFFFF",
    };

    public static String[] interpolated = new String[]{
            "",
            "All your base are belong to us",
            "\\0\\a\\b\\f\\t\\v\\n\\r\\\"{{}}",
            "\u007F\\u0080\\uDEAD\\uFFFF",
    };

    public static String[] interpolatedVerbatim = new String[]{
            "",
            "All your base are belong to us",
            "\0\u0007\b\f\t\u000B\n\r\"\"{{}}",
            "\u007F\u0080\uDEAD\uFFFF",
    };

    //
    // Tests
    //

    @Theory
    public void escapeRegular(@FromDataPoints("regular") String[] rawEscaped) {
        EscapeCSharpRegularAction action = new EscapeCSharpRegularAction();
        assertEquals(rawEscaped[1], action.test_transformByLine(rawEscaped[0]));
    }

    @Theory
    public void escapeVerbatim(@FromDataPoints("verbatim") String[] rawEscaped) {
        EscapeCSharpVerbatimAction action = new EscapeCSharpVerbatimAction();
        assertEquals(rawEscaped[1], action.test_transformByLine(rawEscaped[0]));
    }

    @Theory
    public void escapeInterpolated(@FromDataPoints("interpolated") String[] rawEscaped) {
        EscapeCSharpInterpolatedAction action = new EscapeCSharpInterpolatedAction();
        assertEquals(rawEscaped[1], action.test_transformByLine(rawEscaped[0]));
    }

    @Theory
    public void escapeInterpolatedVerbatim(@FromDataPoints("interpolatedVerbatim") String[] rawEscaped) {
        EscapeCSharpInterpolatedVerbatimAction action = new EscapeCSharpInterpolatedVerbatimAction();
        assertEquals(rawEscaped[1], action.test_transformByLine(rawEscaped[0]));
    }

    //
    // Helpers
    //

    @DataPoints("regular")
    public static String[][] rawToRegular() {
        return rawToEscaped(regular);
    }

    @DataPoints("verbatim")
    public static String[][] rawToVerbatim() {
        return rawToEscaped(verbatim);
    }

    @DataPoints("interpolated")
    public static String[][] rawToInterpolated() {
        return rawToEscaped(interpolated);
    }

    @DataPoints("interpolatedVerbatim")
    public static String[][] rawToInterpolatedVerbatim() {
        return rawToEscaped(interpolatedVerbatim);
    }

    public static String[][] rawToEscaped(String[] escaped) {
        return IntStream
                .range(0, raw.length)
                .mapToObj(i -> new String[] {raw[i], escaped[i]})
                .toArray(String[][]::new);
    }
}
