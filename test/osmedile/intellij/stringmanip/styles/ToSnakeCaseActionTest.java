package osmedile.intellij.stringmanip.styles;

import org.junit.Before;
import org.junit.Test;

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
}