package osmedile.intellij.stringmanip.utils;

import org.junit.Test;
import osmedile.intellij.stringmanip.config.PluginPersistentStateComponent;
import osmedile.intellij.stringmanip.styles.CaseSwitchingTest;

import static org.junit.Assert.assertEquals;

/**
 * StringUtil Tester.
 *
 * @author <Authors name>
 * @version $Id$
 * @since <pre>03/21/2008</pre>
 */
public class StringUtilTest extends CaseSwitchingTest {
    @Test
    public void testToCamelCase() {
        assertEquals("fooBar...", StringUtil.toCamelCase("foo bar..."));
        assertEquals(" fooBarBar1_1 ", StringUtil.toCamelCase(" fooBar bar 1_1 "));
        assertEquals(" fooBar11 ", StringUtil.toCamelCase(" foo bar 1 1 "));
        assertEquals("thisIsAText", StringUtil.toCamelCase("This is a text"));
        //this is ugly but nothing can be done about that.
        assertEquals("whOAhATeSt", StringUtil.toCamelCase("WhOAh a TeSt"));
        assertEquals("whOAhATeSt", StringUtil.toCamelCase("WhOAh_a_TeSt"));
        assertEquals("whOAhATeSt", StringUtil.toCamelCase("WhOAh a_TeSt"));
        assertEquals("11foo22FooBar33Bar44Foo55X6Y7Z", StringUtil.toCamelCase("11Foo22FooBar33Bar44Foo55X6Y7Z"));
        assertEquals("11foo22FooBar33Bar44Foo55X6Y7Z", StringUtil.toCamelCase("11 Foo 22 Foo Bar 33 Bar 44 Foo55 X6 Y7 Z"));
        //previously
//        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh a TeSt"));
//        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh_a_TeSt"));
//        assertEquals("whoahATest", StringUtil.toCamelCase("WhOAh a_TeSt"));
    }

    @Test
    public void testWordsAndCamelToConstantCase() {
        PluginPersistentStateComponent.getInstance().getCaseSwitchingSettings().setSeparatorAfterDigit(true);
        PluginPersistentStateComponent.getInstance().getCaseSwitchingSettings().setSeparatorBeforeDigit(true);
        assertEquals("FOO_BAR... ", StringUtil.wordsAndHyphenAndCamelToConstantCase("fooBar... "));
        assertEquals("V_2_COUNTER", StringUtil.wordsAndHyphenAndCamelToConstantCase("v2Counter"));
        assertEquals("V_22_COUNTER", StringUtil.wordsAndHyphenAndCamelToConstantCase("v22Counter"));
        assertEquals("V_22_COUNTER_22", StringUtil.wordsAndHyphenAndCamelToConstantCase("v22Counter22"));

        PluginPersistentStateComponent.getInstance().getCaseSwitchingSettings().setSeparatorAfterDigit(true);
        PluginPersistentStateComponent.getInstance().getCaseSwitchingSettings().setSeparatorBeforeDigit(false);
        assertEquals("V2_COUNTER", StringUtil.wordsAndHyphenAndCamelToConstantCase("v2Counter"));
        assertEquals("V22_COUNTER", StringUtil.wordsAndHyphenAndCamelToConstantCase("v22Counter"));
        assertEquals("V22_COUNTER22", StringUtil.wordsAndHyphenAndCamelToConstantCase("v22Counter22"));

        StringUtil.persistentStateComponent.getCaseSwitchingSettings().setPutSeparatorBetweenUpperCases(true);
        assertEquals("THIS_IS_A_TEXT", StringUtil.wordsAndHyphenAndCamelToConstantCase("ThisIsAText"));
        assertEquals("WHOAH_A_TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("WhoahATest"));
        assertEquals("WHOAH_A_TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah ATest"));

        StringUtil.persistentStateComponent.getCaseSwitchingSettings().setPutSeparatorBetweenUpperCases(false);
        assertEquals("THIS_IS_ATEXT", StringUtil.wordsAndHyphenAndCamelToConstantCase("ThisIsAText"));
        assertEquals("WHOAH_ATEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("WhoahATest"));
        assertEquals("WHOAH_ATEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah ATest"));
        StringUtil.persistentStateComponent = null;

        assertEquals("WHOAH  A   TEST, AGAIN", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah  A   Test, again"));
        assertEquals("ANOTHER      T_EST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Another      t_Est"));
        assertEquals("TEST_AGAIN     _    _    TEST",
            StringUtil.wordsAndHyphenAndCamelToConstantCase("test again     _    _    test"));
        assertEquals("TEST_AGAIN_   _    TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("TestAgain_   _    Test"));

        //being strict, this will no longer work
//        assertEquals("WHOAH_A_TEST, AGAIN", StringUtil.wordsAndHyphenAndCamelToConstantCase("Whoah  A   Test, again"));
//        assertEquals("ANOTHER_T_EST", StringUtil.wordsAndHyphenAndCamelToConstantCase("Another      t_Est"));
//        assertEquals("TEST_AGAIN_TEST",
//            StringUtil.wordsAndHyphenAndCamelToConstantCase("test again     _    _    test"));
//        assertEquals("TEST_AGAIN_TEST", StringUtil.wordsAndHyphenAndCamelToConstantCase("TestAgain_   _    Test"));
    }

    @Test
    public void testEscapedUnicodeToString() throws Exception {
        assertEquals("Información del diseño", StringUtil.escapedUnicodeToString("Información del diseño"));
        assertEquals("Čás", StringUtil.escapedUnicodeToString("\\u010c\\u00e1s"));
        assertEquals("ďñ", StringUtil.escapedUnicodeToString("\\u010f\\u00f1"));
        assertEquals("abcčd", StringUtil.escapedUnicodeToString("abc\\u010Dd"));
        assertEquals("ěščřžýáíéĚŠČŘŽÝÁÍÉ", StringUtil.escapedUnicodeToString("\\u011B\\u0161\\u010D\\u0159\\u017E\\u00FD\\u00E1\\u00ED\\u00E9\\u011A\\u0160\\u010C\\u0158\\u017D\\u00DD\\u00C1\\u00CD\\u00C9"));
    }

    @Test
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

    @Test
    public void replaceSeparator_keepBetweenDigits() {
        assertEquals("_foo bar 1_1_", StringUtil.replaceSeparatorBetweenLetters("_foo_bar_1_1_", '_', ' '));

    }
}
