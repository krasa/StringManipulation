package osmedile.intellij.stringmanip.align;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextAlignmentActionTest {
	@Test
	public void align() throws Exception {
		assertEquals("foo  ", TextAlignmentAction.Alignment.LEFT.alignLines("foo  "));
		assertEquals("foo  ", TextAlignmentAction.Alignment.LEFT.alignLines(" foo "));
		assertEquals("foo  ", TextAlignmentAction.Alignment.LEFT.alignLines("  foo"));

		assertEquals("  foo", TextAlignmentAction.Alignment.RIGHT.alignLines("foo  "));
		assertEquals("  foo", TextAlignmentAction.Alignment.RIGHT.alignLines(" foo "));
		assertEquals("  foo", TextAlignmentAction.Alignment.RIGHT.alignLines("  foo"));

		assertEquals(" foo ", TextAlignmentAction.Alignment.CENTER.alignLines("foo  "));
		assertEquals(" foo ", TextAlignmentAction.Alignment.CENTER.alignLines(" foo "));
		assertEquals(" foo ", TextAlignmentAction.Alignment.CENTER.alignLines("  foo"));
	}

}