package osmedile.intellij.stringmanip.increment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IncrementActionTest {
	public IncrementAction action = new IncrementAction(false);

	@Test
	public void processSelection() throws Exception {
		check("1|2|3|4", "2|3|4|5");
		check("1", "2");
		check("1.1", "1.2");
		check("1,1", "1,2");
		check("1.1.1", "1.1.2");
		check("1.1,1", "1.1,2");
		check(" 555 5555,5555", " 555 5555,5556");
	}

	@Test
	public void processSelection2() throws Exception {
		check("0001", "0002");
	}

	@Test
	public void processSelection3() throws Exception {
		check("08", "09");
	}

	public void check(String input, String expected) {
		assertEquals(expected, action.processSelection(input));
	}

}