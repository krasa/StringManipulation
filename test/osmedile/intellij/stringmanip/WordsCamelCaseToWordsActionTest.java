package osmedile.intellij.stringmanip;

import org.junit.Assert;
import org.junit.Test;

import osmedile.intellij.stringmanip.unused.WordsCamelCaseToWordsAction;

/**
 * @author Vojtech Krasa
 */
public class WordsCamelCaseToWordsActionTest {
    @Test
    public void testTransform() throws Exception {
        WordsCamelCaseToWordsAction wordsCamelCaseToWordsAction = new WordsCamelCaseToWordsAction(false);
		Assert.assertEquals("Foo bar Wee All", wordsCamelCaseToWordsAction.transformByLine("FooBar Wee All"));
    }
}
