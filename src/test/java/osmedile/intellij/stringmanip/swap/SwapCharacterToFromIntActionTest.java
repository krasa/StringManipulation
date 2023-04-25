package osmedile.intellij.stringmanip.swap;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import osmedile.intellij.stringmanip.CharacterSwitchingSettings;

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
            values[i] = new Object[]{action, "'" + "" + StringEscapeUtils.escapeEcmaScript("" + (char) i) + "'", "" + i, "'" + (i == 39 ? "\\'" : StringEscapeUtils.escapeJava("" + (char) i)) + "'", "'" + (i == 39 ? "\\'" : SwapCharacterToFromIntAction.escapeJava("" + (char) i)) + "'"};
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

}
