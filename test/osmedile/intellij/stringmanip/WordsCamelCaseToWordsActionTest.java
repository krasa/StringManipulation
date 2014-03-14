package osmedile.intellij.stringmanip;

import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vojtech Krasa
 */
public class WordsCamelCaseToWordsActionTest {
	@Test
	
	public void testTransform() throws Exception {
		WordsCamelCaseToWordsAction wordsCamelCaseToWordsAction = new WordsCamelCaseToWordsAction(false);
		Assert.assertEquals("Foo bar Wee All", wordsCamelCaseToWordsAction.transform("FooBar Wee All"));
	}
}
