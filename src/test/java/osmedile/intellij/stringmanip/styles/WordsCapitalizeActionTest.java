package osmedile.intellij.stringmanip.styles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WordsCapitalizeActionTest extends CaseSwitchingTest {
	WordsCapitalizeAction action;


	@Test
	public void transformByLine() {
		action = new WordsCapitalizeAction(false);
		assertEquals("I Am.Fine", action.test_transformByLine("i aM.fine"));

		assertEquals("Foo", action.test_transformByLine("foo"));
		assertEquals("Foo", action.test_transformByLine("FOO"));

		assertEquals("Foo Bar", action.test_transformByLine("foo bar"));
		assertEquals("Foo Bar", action.test_transformByLine("FOO BAR"));

		assertEquals("Foo-bar", action.test_transformByLine("foo-bar"));
		assertEquals("Foo-bar", action.test_transformByLine("FOO-BAR"));

		assertEquals("Foo.Bar", action.test_transformByLine("foo.bar"));
		assertEquals("Foo.Bar", action.test_transformByLine("FOO.BAR"));

		assertEquals("David W.T. Brown", action.test_transformByLine("DAVID W.T. BROWN"));

	}
}