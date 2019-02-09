package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WordsCapitalizeActionTest {
	WordsCapitalizeAction action;


	@Test
	public void transformByLine() {
		action = new WordsCapitalizeAction(false);
		assertEquals("I Am.fine", action.transformByLine("i aM.fine"));
		
		assertEquals("Foo", action.transformByLine("foo"));
		assertEquals("Foo", action.transformByLine("FOO"));

		assertEquals("Foo Bar", action.transformByLine("foo bar"));
		assertEquals("Foo Bar", action.transformByLine("FOO BAR"));

		assertEquals("Foo-bar", action.transformByLine("foo-bar"));
		assertEquals("Foo-bar", action.transformByLine("FOO-BAR"));

		assertEquals("Foo.bar", action.transformByLine("foo.bar"));
		assertEquals("Foo.bar", action.transformByLine("FOO.BAR"));

	}
}