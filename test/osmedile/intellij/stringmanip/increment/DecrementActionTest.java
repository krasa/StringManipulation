package osmedile.intellij.stringmanip.increment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecrementActionTest {
	public DecrementAction action = new DecrementAction(false);

	@Test
	public void processSelection() throws Exception {
		check("000\n" +
						"000\n" +
						"000\n" +
						"000",
				"001\n" +
						"001\n" +
						"001\n" +
						"001"
		);
	}

	@Test
	public void processSelection2() throws Exception {
		check("0001", "0002");
		check("0000", "0001");
	}

	@Test
	public void processSelection3() throws Exception {
		check("08", "09");
	}

	public void check(String expected, String input) {
		assertEquals(expected, action.processSelection(input));
	}

}