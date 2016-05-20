package osmedile.intellij.stringmanip.increment;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class CreateSequenceActionTest {
	public CreateSequenceAction action = new CreateSequenceAction(false);

	@Test
	public void processSelection() throws Exception {
		check("1 2 3 4", "1 1 1 1");
		check("5 6 7 8", "5 2 3 4");
		check("1 2 3 4", "1 0 2 3");
		check("1 2 3 4", "1 -0 -2 -3");
	}

	public void check(String expected, String input) {
		assertEquals(expected, action.processSelection(input, new AtomicReference<String>()));
	}

}

