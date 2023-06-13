package osmedile.intellij.stringmanip.styles;

import org.junit.Before;
import org.junit.Test;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;

import static org.junit.Assert.assertEquals;

public class ToSnakeCaseActionTest {
	ToSnakeCaseAction action;

	@Before
	public void setUp() throws Exception {
		action = new ToSnakeCaseAction(false);
	}

	@Test
	public void transformByLine() {
		assertEquals("lorem_ipsum_dolor_sit", action.test_transformByLine("lorem.ipsum_dolor.sit"));
	}

	@Test
	public void transformByLine2() {
		try {
			CaseSwitchingSettings.getInstance().setPutSeparatorBetweenUpperCases(true);

			assertEquals("ord_t_seq", action.test_transformByLine("ordTSeq"));
		} finally {
			CaseSwitchingSettings.getInstance().setPutSeparatorBetweenUpperCases(false);
		}
	}
}