package osmedile.intellij.stringmanip.swap;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import osmedile.intellij.stringmanip.CharacterSwitchingSettings;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SwapCharacterToFromIntActionTest {
    private final SwapCharacterToFromIntAction action;
    private final String testCharValue;
    private final String testIntValue;
    private final String testLiteralUnicodeValue;
    private final String testLiteralOctalCharValue;

    public SwapCharacterToFromIntActionTest(SwapCharacterToFromIntAction action, String testCharValue, String testIntValue, String testLiteralUnicodeValue, String testLiteralOctalCharValue) {
        this.action = action;
        this.testCharValue = testCharValue;
        this.testIntValue = testIntValue;
        this.testLiteralUnicodeValue = testLiteralUnicodeValue;
        this.testLiteralOctalCharValue = testLiteralOctalCharValue;
    }

    @Parameters(name = "{index}: {1} {2} {3} {4}")
    public static List<Object[]> testValues() {
        SwapCharacterToFromIntAction action = new SwapCharacterToFromIntAction();
        Object[][] values = new Object[256][];
        for (int i = 0; i < 256; i++) {
            values[i] = new Object[]{action, "'" + "" + StringEscapeUtils.escapeEcmaScript("" + (char) i) + "'", "" + i, "'" + (i == 39 ? "\\'" : StringEscapeUtils.escapeJava("" + (char) i)) + "'", "'" + (i == 39 ? "\\'" : escapeJava("" + (char) i)) + "'"};
        }
        return Arrays.asList(values);
    }

    @Test
    public void testCharToNumber() {
        String test = transform(testCharValue);
        assertEquals(test, testIntValue);
    }

    @Test
    public void testNumberToCharUnicode() {
        action.setForceEncoding(CharacterSwitchingSettings.Encoding.UNICODE);
        String test = transform(testIntValue);
        assertEquals(test, testLiteralUnicodeValue);
    }

    @Test
    public void testNumberToCharOctal() {
        action.setForceEncoding(CharacterSwitchingSettings.Encoding.OCTAL);
        String test = transform(testIntValue);
        assertEquals(test, testLiteralOctalCharValue);
    }

    private String transform(String input) {
        return action.transformSelection(null, null, null, input, null);
    }


    private static String escapeJava(String str) {
        return escapeJavaStyleString(str);
    }

    private static String escapeJavaStyleString(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter writer = new StringWriter(str.length() * 2);
                escapeJavaStyleString(writer, str);
                return writer.toString();
            } catch (IOException var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    private static void escapeJavaStyleString(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);
                if (ch > 127) {
                    out.write("\\" + oct(ch));
                } else if (ch < 32) {
                    switch (ch) {
                        case '\b':
                            out.write(92);
                            out.write(98);
                            break;
                        case '\t':
                            out.write(92);
                            out.write(116);
                            break;
                        case '\n':
                            out.write(92);
                            out.write(110);
                            break;
                        case '\u000b':
                        default:
                            out.write("\\" + oct(ch));
                            break;
                        case '\f':
                            out.write(92);
                            out.write(102);
                            break;
                        case '\r':
                            out.write(92);
                            out.write(114);
                    }
                } else {
                    switch (ch) {
                        case '"':
                            out.write(92);
                            out.write(34);
                            break;
                        case '\'':
                            out.write(92);
                            out.write(39);
                            break;
                        case '/':
                            out.write(92);
                            out.write(47);
                            break;
                        case '\\':
                            out.write(92);
                            out.write(92);
                            break;
                        default:
                            out.write(ch);
                    }
                }
            }
        }
    }

    private static String oct(char ch) {
        return Integer.toString(ch, 8);
    }

}
