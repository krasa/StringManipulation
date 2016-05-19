package osmedile.intellij.stringmanip.increment;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class CreateSequenceActionTest {
	public CreateSequenceAction action = new CreateSequenceAction(false);

	@Test
	public void processSelection() throws Exception {
		assertEquals("1 2 3 4", run("1 1 1 1"));
		assertEquals("5 6 7 8", run("5 2 3 4"));
		assertEquals("1 2 3 4", run("1 0 2 3"));
		assertEquals("1 2 3 4", run("1 -0 -2 -3"));
	}

	public String run(String selectedText) {
		return action.processSelection(selectedText, new AtomicReference<String>());
	}

}

