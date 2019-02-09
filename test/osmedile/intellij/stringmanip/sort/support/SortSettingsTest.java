package osmedile.intellij.stringmanip.sort.support;

import org.junit.Assert;
import org.junit.Test;

public class SortSettingsTest {
	@Test
	public void test() throws Exception {
		SortSettings sortSettings = new SortSettings(Sort.LINE_LENGTH_SHORT_LONG).ignoreLeadingSpaces(true).preserveLeadingSpaces(true).preserveTrailingSpecialCharacters(true).trailingChars(",;'|;[p;/|");

		Assert.assertEquals("NATURAL|LINE_LENGTH_SHORT_LONG|REMOVE|true|true|true|,;'|;[p;/|", sortSettings.asString());
		Assert.assertEquals(sortSettings, sortSettings.fromString(sortSettings.asString()));
	}

	@Test
	public void test2() throws Exception {
		SortSettings sortSettings = new SortSettings(Sort.LINE_LENGTH_SHORT_LONG).ignoreLeadingSpaces(true).preserveLeadingSpaces(true).preserveTrailingSpecialCharacters(true).trailingChars(",;'|;[p;/|");

		Assert.assertEquals(sortSettings, sortSettings.fromString("LINE_LENGTH_SHORT_LONG|REMOVE|true|true|true|,;'|;[p;/|"));
		Assert.assertEquals(sortSettings, sortSettings.fromString("NATURAL|LINE_LENGTH_SHORT_LONG|REMOVE|true|true|true|,;'|;[p;/|"));
	}

}