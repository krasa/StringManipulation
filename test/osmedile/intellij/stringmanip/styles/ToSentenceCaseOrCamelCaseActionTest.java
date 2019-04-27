package osmedile.intellij.stringmanip.styles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToSentenceCaseOrCamelCaseActionTest {
	ToSentenceCaseOrCamelCaseAction action;

	@Before
	public void setUp() throws Exception {
		action = new ToSentenceCaseOrCamelCaseAction(false);
	}

	@Test
	public void transformByLine() {
		assertEquals("I a m fine", action.transformByLine("i aM.fine"));

		assertEquals("Foo", action.transformByLine("foo"));
		assertEquals("Foo", action.transformByLine("FOO"));

		assertEquals("Foo bar", action.transformByLine("foo bar"));
		assertEquals("Foo bar", action.transformByLine("FOO BAR"));

		assertEquals("Foo bar", action.transformByLine("foo-bar"));
		assertEquals("Foo bar", action.transformByLine("FOO-BAR"));

		assertEquals("Foo bar", action.transformByLine("foo.bar"));
		assertEquals("Foo bar", action.transformByLine("FOO.BAR"));

		assertEquals("fooBar", action.transformByLine("Foo bar"));
		assertEquals("fooBar", action.transformByLine("Foo bar."));
		assertEquals("foo,Bar", action.transformByLine("Foo, bar."));
		assertEquals("Foo,bar", action.transformByLine("foo,Bar"));
		assertEquals("foo,Bar", action.transformByLine("Foo,bar"));
	}
}