package com.github.krasa.stringmanipulation.intellij.align;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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