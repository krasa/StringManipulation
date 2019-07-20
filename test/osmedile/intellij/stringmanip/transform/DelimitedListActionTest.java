package osmedile.intellij.stringmanip.transform;

import org.junit.Assert;
import org.junit.Test;

public class DelimitedListActionTest {

	@Test
	public void fromCsv() {
		DelimitedListAction.Settings settings = new DelimitedListAction.Settings();
		settings.unquote = "";
		settings.quote = "\"";
		settings.sourceDelimiter = ",";
		settings.destinationDelimiter = ",";
		settings.source = "";

		String s = new DelimitedListAction(false).transformText("1,2,3\n1,2,3\n", settings);
		Assert.assertEquals("\"1\",\"2\",\"3\"\n" +
			"\"1\",\"2\",\"3\"\n", s);
	}
}