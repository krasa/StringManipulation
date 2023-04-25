package osmedile.intellij.stringmanip.styles.switching;

import org.junit.Assert;
import org.junit.Test;
import osmedile.intellij.stringmanip.styles.CaseSwitchingTest;

public class ToWordCapitalizedOrCamelCaseActionTest extends CaseSwitchingTest {
	private static final ToWordCapitalizedOrCamelCaseAction ACTION = new ToWordCapitalizedOrCamelCaseAction(false);

	@Test
	public void transformByLine() throws Exception {
		Assert.assertEquals("David W T. Brown", ACTION.test_transformByLine("DAVID W.T. BROWN"));
	}

}