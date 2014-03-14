package osmedile.intellij.stringmanip.utils;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

/**
 * DuplicatUtils Tester.
 *
 * @version $Id: DuplicatUtilsTest.java 40 2008-03-26 21:08:33Z osmedile $
 * @since <pre>12/25/2007</pre>
 */
public class DuplicatUtilsTest extends TestCase {
    public DuplicatUtilsTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(DuplicatUtilsTest.class);
    }


    public void testIncrementNumeric() {
        assertEquals("101", DuplicatUtils.simpleInc("100"));
        assertEquals("101.3", DuplicatUtils.simpleInc("100.3"));
        assertEquals("101.3", DuplicatUtils.simpleInc("100.3d"));

        assertEquals("4", DuplicatUtils.simpleInc("03"));
    }

    /**
     * :    2   { "boo", ":", "and:foo" }
     * :    5   { "boo", ":", "and", ":", "foo"}
     * :    -2  { "boo", ":", "and", ":", "foo"}
     * o    5   { "b", "oo", ":and:f", "o", "o"}
     * o    -2  { "b", "", ":and:f", "", ""}
     * o    0   { "b", "", ":and:f"}
     */
    public void testSplitPreserveAllTokens() {
        aTestSplitPreserveAllTokens("boo:and:foo", ":", "boo", ":", "and", ":", "foo");
        aTestSplitPreserveAllTokens("boo:and:", ":", "boo", ":", "and", ":");
        aTestSplitPreserveAllTokens("foo", ":", "foo");
        aTestSplitPreserveAllTokens(":booand:foo", ":", ":", "booand", ":", "foo");
        aTestSplitPreserveAllTokens(":boo:and:foo:", ":", ":", "boo", ":", "and", ":", "foo", ":");

        aTestSplitPreserveAllTokens("toto88titi65", "[0-9]+", "toto", "88", "titi", "65");
        aTestSplitPreserveAllTokens("77toto88titi65", "[0-9]+", "77", "toto", "88", "titi", "65");
        aTestSplitPreserveAllTokens("775", "[0-9]+", "775");
        aTestSplitPreserveAllTokens("toto", "[0-9]+", "toto");

        //Test with real strings
        aTestSplitPreserveAllTokens("hexa bob1toto0x23 12 234titito1.23 et le héron22dfsfs0x1.23df",
                DuplicatUtils.HEXA_ONLY_REGEX, "hexa bob1toto", "0x23",
                " 12 234titito1.23 et le héron22dfsfs", "0x1", ".23df");
        aTestSplitPreserveAllTokens("number bob1toto0x23 12 234titito1.23 et le héron22dfsfs0x1.23df",
                DuplicatUtils.INT_ONLY_REGEX, "number bob", "1", "toto", "0", "x", "23", " ", "12", " ",
                "234", "titito", "1", ".", "23", " et le héron", "22d", "fsfs", "0", "x", "1", ".", "23d",
                "f");
        aTestSplitPreserveAllTokens("number bob-1toto0x23 12 2-34titito1.23 et le héron22dfsfs0x1.-23df",
                DuplicatUtils.INT_ONLY_REGEX, "number bob", "-1", "toto", "0", "x", "23", " ", "12", " ", "2",
                "-34", "titito", "1", ".", "23", " et le héron", "22d", "fsfs", "0", "x", "1", ".", "-23d",
                "f");
        aTestSplitPreserveAllTokens("decimal bob1toto0x23 12 234titito1.23 et le héron22dfsfs0x1.23df",
                DuplicatUtils.DECIMAL_ONLY_REGEX, "decimal bob1toto0x23 12 234titito", "1.23",
                " et le héron22dfsfs0x", "1.23d", "f");
        aTestSplitPreserveAllTokens("decimal bob-1toto0x23 12 234titito-1.23 et le héron22dfsfs0x1-.23df",
                DuplicatUtils.DECIMAL_ONLY_REGEX, "decimal bob-1toto0x23 12 234titito", "-1.23",
                " et le héron22dfsfs0x1", "-.23d", "f");
        aTestSplitPreserveAllTokens(
                "sci bob1.dtoto0x23 12 234ti-.29E30tito-1.23e-34et . le héron-22e20dfsfs0x1.23df",
                DuplicatUtils.SCI_ONLY_REGEX, "sci bob1.dtoto0x23 12 234ti", "-.29E30", "tito", "-1.23e-34",
                "et . le héron", "-22e20", "dfsfs0x1.23df");
        aTestSplitPreserveAllTokens(
                "nb bob1.dtoto0x23 12 234ti-.29E30tito-1.23e-34et . le héron-22e20dfsfs0x1.23df",
                DuplicatUtils.NUMBER_REGEX, "nb bob", "1.d", "toto", "0x23", " ", "12", " ", "234", "ti",
                "-.29E30", "tito", "-1.23e-34", "et . le héron", "-22e20", "dfsfs", "0x1", ".23d", "f");
        aTestSplitPreserveAllTokens("nbTwo bob1.dto.to0x23 12 234ti-.29E30the1Ltito-1.23e-34et",
                DuplicatUtils.NUMBER_REGEX, "nbTwo bob", "1.d", "to.to", "0x23", " ", "12", " ", "234", "ti",
                "-.29E30", "the", "1L", "tito", "-1.23e-34", "et");
    }

    public void aTestSplitPreserveAllTokens(String input, String regex, String... expected) {
        assertArraysEquals(expected, StringUtil.splitPreserveAllTokens(input, regex));
    }

    public void assertArraysEquals(Object[] array1, Object[] array2) {
        if (!Arrays.equals(array1, array2)) {
            throw new AssertionFailedError(
                    "Expected " + Arrays.toString(array1) + " but was " + Arrays.toString(array2));
        }
    }
}
