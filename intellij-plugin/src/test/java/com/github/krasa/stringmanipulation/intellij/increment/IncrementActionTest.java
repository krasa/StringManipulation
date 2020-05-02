package com.github.krasa.stringmanipulation.intellij.increment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IncrementActionTest {
	public IncrementAction action = new IncrementAction(false);

	@Test
	public void processSelection() throws Exception {
		check("1 2 3 4", "2 3 4 5");
		check("1", "2");
//		check("1.1", "1.2");
//		check("1,1", "1,2");
//		check("1.1.1", "1.1.2");
//		check("1.1,1", "1.1,2");
	}

	public void check(String input, String expected) {
		assertEquals(expected, action.processSelection(input));
	}

}