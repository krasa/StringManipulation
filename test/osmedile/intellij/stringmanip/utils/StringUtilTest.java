package osmedile.intellij.stringmanip.utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * StringUtil Tester.
 *
 * @author <Authors name>
 * @version $Id$
 * @since <pre>03/21/2008</pre>
 */
public class StringUtilTest extends TestCase {

    public StringUtilTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(StringUtilTest.class);
    }

    public void testToSoftCamelCase() {
        assertEquals("ThisIsAText", StringUtil.toSoftCamelCase("This is a text"));
        assertEquals("WhOAhATeSt", StringUtil.toSoftCamelCase("WhOAh a TeSt"));
        assertEquals("WhOAhATeSt", StringUtil.toSoftCamelCase("WhOAh_a_TeSt"));
        assertEquals("WhOAhATeSt", StringUtil.toSoftCamelCase("WhOAh a_TeSt"));
    }

    public void testToCamelCase() {
        assertEquals("thisIsAText", StringUtil.toCamelCase("This is a text"));
        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh a TeSt"));
        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh_a_TeSt"));
        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh a_TeSt"));
    }

    public void testWordsAndCamelToConstantCase() {
        assertEquals("THIS_IS_A_TEXT", StringUtil.wordsAndHyphenAndCamelToConstantCase("ThisIsAText")); //todo it is broken :(
        assertEquals("WHOAH_A_TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("WhoahATest"));
        assertEquals("WHOAH_A_TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah ATest"));
        assertEquals("WHOAH_A_TEST,_AGAIN", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah  A   Test, again"));
        assertEquals("ANOTHER_T_EST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Another      t_Est"));
        assertEquals("TEST_AGAIN__TEST",
                StringUtil.wordsAndHyphenAndCamelToConstantCase("test again     _    _    test"));
        assertEquals("TEST_AGAIN__TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("TestAgain_   _    Test"));
    }

    public void testEscapedUnicodeToString() throws Exception {
        assertEquals("Información del diseño", StringUtil.escapedUnicodeToString("Información del diseño"));
        assertEquals("Čás", StringUtil.escapedUnicodeToString("\\u010c\\u00e1s"));
        assertEquals("ďñ", StringUtil.escapedUnicodeToString("\\u010f\\u00f1"));
        assertEquals("abcčd", StringUtil.escapedUnicodeToString("abc\\u010Dd"));
        assertEquals("ěščřžýáíéĚŠČŘŽÝÁÍÉ", StringUtil.escapedUnicodeToString("\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9\\u011A\\u0160\\u010C\\u0158\\u017D\\u00DD\\u00C1\\u00CD\\u00C9"));
    }

    public void testWordsToConstantCase() {
        assertEquals("THISISATEXT", StringUtil.wordsToConstantCase("ThisIsAText"));
        assertEquals("WHOAH_A_TEST", StringUtil.wordsToConstantCase("Whoah A Test"));
        assertEquals("WHOAH_A_TEST", StringUtil.wordsToConstantCase("Whoah    a tESt"));
        assertEquals("_ANOTHER_TEXT_", StringUtil.wordsToConstantCase("_ANOTHER     TExT_"));
        assertEquals("TEST_AGAIN_____TEST",
                StringUtil.wordsToConstantCase("test agaIN     _    _    _    _    _    test"));
        assertEquals("TEST_AGAIN_____TEST",
                StringUtil.wordsToConstantCase("test agaIN_    _    _    _    _    Test"));
        assertEquals("TEST_AGAIN", StringUtil.wordsToConstantCase(" test agaIN"));
        assertEquals("_TEST_AGAIN", StringUtil.wordsToConstantCase("_  test agaIN"));
        assertEquals("_TEST_AGAIN", StringUtil.wordsToConstantCase("   _  test agaIN"));
    }

    public void testWordsToHyphenCase() {
        assertEquals("this-is-a-text", StringUtil.wordsToHyphenCase("THIS IS A TEXT"));
        assertEquals("whoah-a-test", StringUtil.wordsToHyphenCase("Whoah A Test"));
        assertEquals("whoah-a-test", StringUtil.wordsToHyphenCase("Whoah    a tESt"));
    }

    public void testToDotCase() {
        assertEquals("this.is.a.text", StringUtil.toDotCase("THIS IS A TEXT"));
        assertEquals("whoah.a.test", StringUtil.toDotCase("Whoah A   Test"));
        assertEquals("whoah.a...test", StringUtil.toDotCase("Whoah A - _ Test"));
        assertEquals("whoah.a.t.est", StringUtil.toDotCase("Whoah    a tESt")); //todo: bug인지 확인 필요
    }

    public void testWordsToUnderscoreStringInJava() {
        assertEquals("THIS_IS_A_TEXT", StringUtil.wordsToUnderscoreCase("THIS IS A TEXT"));
        assertEquals("THISISATEXT", StringUtil.wordsToUnderscoreCase("THIS--IS-A-TEXT"));
        assertEquals("THIS_IS_A_TEXT", StringUtil.wordsToUnderscoreCase("THIS   IS A   TEXT"));
        assertEquals("A_B_", StringUtil.wordsToUnderscoreCase("A_B "));
        assertEquals("A_B", StringUtil.wordsToUnderscoreCase("A_B"));
        assertEquals("A_B", StringUtil.wordsToUnderscoreCase("A  B"));
        assertEquals("A_B", StringUtil.wordsToUnderscoreCase("A__B"));
        assertEquals("ThisIsAText", StringUtil.wordsToUnderscoreCase("ThisIsAText"));
        assertEquals("This_Is_A_Text", StringUtil.wordsToUnderscoreCase("This_Is_A__Text"));
        assertEquals("Whoah_A_Test", StringUtil.wordsToUnderscoreCase("Whoah A Test"));
        assertEquals("Whoah_a_tESt", StringUtil.wordsToUnderscoreCase("Whoah    a tESt"));
        assertEquals("singleLine_clientA는_removeFormat하고_clientB는_EndOffset에_table_삽입하는_경우", StringUtil.wordsToUnderscoreCase("singleLine - clientA는 removeFormat하고, clientB는 EndOffset에 table 삽입하는 경우"));
        assertEquals("multiLine_clientA는_removeFormat_newLine_하고_clientB는_EndOffset에_table_삽입하는_경우", StringUtil.wordsToUnderscoreCase("multiLine - clientA는 removeFormat(newLine)하고, clientB는 EndOffset에 table 삽입하는 경우"));
    }
}
