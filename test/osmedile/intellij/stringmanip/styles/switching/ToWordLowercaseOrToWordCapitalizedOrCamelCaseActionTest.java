package osmedile.intellij.stringmanip.styles.switching;

import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.CaseSwitchingTest;

public class ToWordLowercaseOrToWordCapitalizedOrCamelCaseActionTest extends CaseSwitchingTest {
    private static final ToWordLowercaseOrToCamelCaseAction ACTION = new ToWordLowercaseOrToCamelCaseAction(false);

    @Test
    public void transformByLine() throws Exception {
        Assert.assertEquals("foo bar", ACTION.test_transformByLine("fooBar"));
        Assert.assertEquals("fooBar", ACTION.test_transformByLine("foo bar"));
        Assert.assertEquals("david w t. brown", ACTION.test_transformByLine("DAVID W.T. BROWN"));
    }

}