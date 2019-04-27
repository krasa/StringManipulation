package osmedile.intellij.stringmanip.styles;

import org.junit.Assert;
import org.junit.Test;

public class ToWordLowercaseOrToWordCapitalizedOrCamelCaseActionTest extends CaseSwitchingTest {
    private static final ToWordLowercaseOrToCamelCaseAction ACTION = new ToWordLowercaseOrToCamelCaseAction(false);

    @Test
    public void transformByLine() throws Exception {
        Assert.assertEquals("foo bar", ACTION.transformByLine("fooBar"));
        Assert.assertEquals("fooBar", ACTION.transformByLine("foo bar"));
    }

}