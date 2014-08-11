package osmedile.intellij.stringmanip.styles;

import static org.junit.Assert.*;

import org.junit.Test;

public class ToCamelCaseActionTest {

	@Test
	public void testTransform() throws Exception {
		assertEquals("foo", new ToCamelCaseAction(false).transform("foo"));
	}
}