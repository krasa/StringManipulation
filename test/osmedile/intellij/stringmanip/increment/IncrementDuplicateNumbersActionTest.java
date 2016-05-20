package osmedile.intellij.stringmanip.increment;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class IncrementDuplicateNumbersActionTest {
	public IncrementDuplicateNumbersAction action = new IncrementDuplicateNumbersAction(false);

	@Test
	public void processSelection() throws Exception {
		check("1 2 3 4", "1 1 1 1");
		check("1 2 3 4", "1 1 2 2");
		check("5 2 3 4", "5 2 3 4");
		check("1 0 2 3", "1 0 2 3");
	}

	public void check(String expected, String input) {
		assertEquals(expected, action.processSelection(input, new HashSet<String>()));
	}

}