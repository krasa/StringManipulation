package osmedile.intellij.stringmanip.styles;

import org.junit.Assert;
import org.junit.Test;

public class ToWordCapitalizedOrCamelCaseActionTest extends CaseSwitchingTest {
	private static final ToWordCapitalizedOrCamelCaseAction ACTION = new ToWordCapitalizedOrCamelCaseAction(false);

	@Test
	public void transformByLine() throws Exception {
		Assert.assertEquals("David W T. Brown", ACTION.transformByLine("DAVID W.T. BROWN"));
	}

}