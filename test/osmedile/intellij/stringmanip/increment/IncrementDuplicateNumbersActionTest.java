package osmedile.intellij.stringmanip.increment;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

public class IncrementDuplicateNumbersActionTest {
	public IncrementDuplicateNumbersAction action = new IncrementDuplicateNumbersAction(false);

	@Test
	public void processSelection() throws Exception {
		assertEquals("1 2 3 4", run("1 1 1 1"));
		assertEquals("1 2 3 4", run("1 1 2 2"));
		assertEquals("5 2 3 4", run("5 2 3 4"));
		assertEquals("1 0 2 3", run("1 0 2 3"));

	}

	public String run(String selectedText) {
		return action.processSelection(selectedText, new HashSet<String>());
	}

}