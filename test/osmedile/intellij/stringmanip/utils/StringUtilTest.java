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

    public void testToCamelCase() {
        assertEquals("thisIsAText", StringUtil.toCamelCase("This is a text"));
        //this is ugly but nothing can be done about that.
        assertEquals("whOAhATeSt", StringUtil.toCamelCase("WhOAh a TeSt"));
        assertEquals("whOAhATeSt", StringUtil.toCamelCase("WhOAh_a_TeSt"));
        assertEquals("whOAhATeSt", StringUtil.toCamelCase("WhOAh a_TeSt"));
        //previously
//        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh a TeSt"));
//        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh_a_TeSt"));
//        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh a_TeSt"));
    }

    public void testWordsAndCamelToConstantCase() {
        assertEquals("V2_COUNTER", StringUtil.wordsAndHyphenAndCamelToConstantCase("v2Counter"));
        assertEquals("V22_COUNTER", StringUtil.wordsAndHyphenAndCamelToConstantCase("v22Counter"));
        assertEquals("V22_COUNTER22", StringUtil.wordsAndHyphenAndCamelToConstantCase("v22Counter22"));
		assertEquals("THIS_IS_ATEXT", StringUtil.wordsAndHyphenAndCamelToConstantCase("ThisIsAText"));
		assertEquals("WHOAH_ATEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("WhoahATest"));
		assertEquals("WHOAH_ATEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah ATest"));
        assertEquals("WHOAH_A_TEST_AGAIN", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah  A   Test, again"));
        assertEquals("ANOTHER_T_EST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Another      t_Est"));
        assertEquals("TEST_AGAIN_TEST",
                StringUtil.wordsAndHyphenAndCamelToConstantCase("test again     _    _    test"));
        assertEquals("TEST_AGAIN_TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("TestAgain_   _    Test"));
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
}
