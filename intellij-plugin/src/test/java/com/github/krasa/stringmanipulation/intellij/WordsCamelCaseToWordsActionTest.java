package com.github.krasa.stringmanipulation.intellij;

import com.github.krasa.stringmanipulation.intellij.unused.WordsCamelCaseToWordsAction;
import org.junit.Assert;
import org.junit.Test;

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
