package osmedile.intellij.stringmanip.styles.switching;

import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.CaseSwitchingTest;

import static org.junit.Assert.assertEquals;

public class ToWordLowercaseOrToWordCapitalizedOrCamelCaseActionTest extends CaseSwitchingTest {
	private static final ToWordLowercaseOrToCamelCaseAction ACTION = new ToWordLowercaseOrToCamelCaseAction(false);

	@Test
	public void transformByLine() throws Exception {
		Assert.assertEquals("foo bar", ACTION.test_transformByLine("fooBar"));
		Assert.assertEquals("fooBar", ACTION.test_transformByLine("foo bar"));
		Assert.assertEquals("david w t. brown", ACTION.test_transformByLine("DAVID W.T. BROWN"));
	}

	@Test
	public void testTransform3() throws Exception {
		caseSwitchingSettings.setSeparatorBeforeDigit(true);

		String actual = ACTION.test_transform("currency\n" +
				"code\n" +
				"Official Rate\n" +
				"rate_buy\n" +
				"sym\n" +
				"symInt\n" +
				"inFront\n" +
				"coin");

		assertEquals("currency\n" +
				"code\n" +
				"official rate\n" +
				"rate buy\n" +
				"sym\n" +
				"sym int\n" +
				"in front\n" +
				"coin", actual);

		actual = ACTION.test_transform(actual);
		assertEquals("currency\n" +
				"code\n" +
				"officialRate\n" +
				"rateBuy\n" +
				"sym\n" +
				"symInt\n" +
				"inFront\n" +
				"coin", actual);

	}
}