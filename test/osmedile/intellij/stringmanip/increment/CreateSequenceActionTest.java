package osmedile.intellij.stringmanip.increment;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class CreateSequenceActionTest {
	public CreateSequenceAction action = new CreateSequenceAction(false);

	@Test
	public void processSelection() throws Exception {
		test("1 2 3 4", "1 1 1 1");
		test("5 6 7 8", "5 2 3 4");
		test("1 2 3 4", "1 0 2 3");
		test("1 2 3 4", "1 -0 -2 -3");
	}

	public void test(String expected, String input) {
		assertEquals(expected, action.processSelection(input, new AtomicReference<String>()));
	}

}

