package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WordsCapitalizeActionTest extends CaseSwitchingTest {
	WordsCapitalizeAction action;


	@Test
	public void transformByLine() {
		action = new WordsCapitalizeAction(false);
		assertEquals("I Am.Fine", action.transformByLine("i aM.fine"));

		assertEquals("Foo", action.transformByLine("foo"));
		assertEquals("Foo", action.transformByLine("FOO"));

		assertEquals("Foo Bar", action.transformByLine("foo bar"));
		assertEquals("Foo Bar", action.transformByLine("FOO BAR"));

		assertEquals("Foo-bar", action.transformByLine("foo-bar"));
		assertEquals("Foo-bar", action.transformByLine("FOO-BAR"));

		assertEquals("Foo.Bar", action.transformByLine("foo.bar"));
		assertEquals("Foo.Bar", action.transformByLine("FOO.BAR"));

		assertEquals("David W.T. Brown", action.transformByLine("DAVID W.T. BROWN"));

	}
}