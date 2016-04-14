package osmedile.intellij.stringmanip.align;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextAlignmentActionTest {
	@Test
	public void align() throws Exception {
		TextAlignmentAction action = new TextAlignmentAction(false, null) {
		};

		assertEquals("foo  ", TextAlignmentAction.Alignment.LEFT.align("foo  "));
		assertEquals("foo  ", TextAlignmentAction.Alignment.LEFT.align(" foo "));
		assertEquals("foo  ", TextAlignmentAction.Alignment.LEFT.align("  foo"));

		assertEquals("  foo", TextAlignmentAction.Alignment.RIGHT.align("foo  "));
		assertEquals("  foo", TextAlignmentAction.Alignment.RIGHT.align(" foo "));
		assertEquals("  foo", TextAlignmentAction.Alignment.RIGHT.align("  foo"));

		assertEquals(" foo ", TextAlignmentAction.Alignment.CENTER.align("foo  "));
		assertEquals(" foo ", TextAlignmentAction.Alignment.CENTER.align(" foo "));
		assertEquals(" foo ", TextAlignmentAction.Alignment.CENTER.align("  foo"));
	}

}